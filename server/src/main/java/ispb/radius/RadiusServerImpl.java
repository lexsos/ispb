package ispb.radius;

import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import ispb.base.radius.RadiusServer;
import ispb.base.radius.RadiusServlet;
import ispb.base.resources.Config;
import ispb.base.service.LogService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.*;


public class RadiusServerImpl implements RadiusServer {

    private final Config config;
    private final LogService logService;

    private volatile boolean started;
    private DatagramSocket authSocket = null;
    private final BlockingQueue<DatagramPacket> requestAuthQueue;

    private final ExecutorService workers;
    private final ExecutorService listeners;
    private final int workerTimeout;
    private final int workerCount;
    private final Map<InetAddress, RadiusServlet> servletMap;

    private class Listener implements Runnable {

        public void run(){

            while (isStarted() && !getAuthSocket().isClosed()){

                DatagramPacket packetIn = new DatagramPacket(new byte[RadiusPacket.MAX_PACKET_LENGTH], RadiusPacket.MAX_PACKET_LENGTH);
                try {
                    getAuthSocket().receive(packetIn);
                    if (!offerAuthPacket(packetIn))
                        getLogService().info("Discard RADIUS auth packet from " + packetIn.getAddress());
                    else
                        getLogService().debug("Receive RADIUS auth packet from " + packetIn.getAddress());
                }
                catch (SocketTimeoutException ignored){
                }
                catch (Throwable e){
                    getLogService().warn("Interrupted while wait RADIUS auth packet", e);
                }
            }
        }
    }

    private class AuthHandler implements Runnable {

        public void run(){

            while (isStarted()){

                DatagramPacket requestDatagram = pullAuth();
                if (requestDatagram == null)
                    continue;

                InetAddress clientAddress = requestDatagram.getAddress();
                int clientPort = requestDatagram.getPort();

                RadiusServlet servlet = getServlet(clientAddress);
                if (servlet == null) {
                    getLogService().info("Discard RADIUS auth packet, not found servlet for RADIUS client " + clientAddress);
                    continue;
                }

                String sharedSecret = servlet.getSharedSecret(clientAddress);
                if (sharedSecret == null || sharedSecret.length() == 0){
                    getLogService().info("Discard RADIUS auth packet, shared secret is empty for " + clientAddress);
                    continue;
                }

                RadiusPacket radiusRequest;

                try {
                    radiusRequest = makeRadiusPacket(requestDatagram, sharedSecret);
                }
                catch (RadiusException e){
                    getLogService().warn("Error while parse RADIUS packet from" + clientAddress, e);
                    continue;
                }
                catch (IOException e){
                    getLogService().warn("Can't read packet from " + clientAddress, e);
                    continue;
                }

                RadiusPacket radiusResponse;
                try {
                    radiusResponse = servlet.service(radiusRequest, clientAddress, sharedSecret);
                }
                catch (Throwable e){
                    getLogService().warn("Error while execute RADIUS servlet for " + clientAddress, e);
                    continue;
                }

                if (radiusResponse == null)
                    continue;

                copyProxyState(radiusRequest, radiusResponse);

                try {
                    DatagramPacket answer = makeDatagramPacket(
                            radiusResponse,
                            sharedSecret,
                            clientAddress,
                            clientPort,
                            radiusRequest);
                    sendAnswer(answer);
                }
                catch (IOException e){
                    getLogService().warn("Can't send packet to " + clientAddress, e);
                }
            }
        }

        private RadiusPacket makeRadiusPacket(DatagramPacket packet, String sharedSecret) throws
                IOException, RadiusException {
            InputStream in = new ByteArrayInputStream(packet.getData());
            return RadiusPacket.decodeRequestPacket(in, sharedSecret);
        }

        @SuppressWarnings("Convert2streamapi")
        private void copyProxyState(RadiusPacket request, RadiusPacket answer) {
            for (Object attribute: request.getAttributes(33)){
                if (attribute instanceof RadiusAttribute)
                    answer.addAttribute((RadiusAttribute)attribute);
            }
        }

        private DatagramPacket makeDatagramPacket(RadiusPacket packet,
                                                  String secret,
                                                  InetAddress address,
                                                  int port,
                                                  RadiusPacket request)
                throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            packet.encodeResponsePacket(bos, secret, request);
            byte[] data = bos.toByteArray();

            return new DatagramPacket(data, data.length, address, port);
        }
    }

    public RadiusServerImpl(Config config, LogService logService){
        this.config = config;
        this.logService = logService;
        workerTimeout = config.getAsInt("radius.workerTimeout");
        workerCount = config.getAsInt("radius.workerCount");
        int queueSize = config.getAsInt("radius.queueSize");

        requestAuthQueue = new ArrayBlockingQueue<>(queueSize, true);
        workers = Executors.newFixedThreadPool(workerCount);
        listeners = Executors.newFixedThreadPool(1);
        servletMap = new ConcurrentHashMap<>();
    }

    public void start(){
        authSocket = createAuthSocket();
        if (authSocket == null)
            return;

        started = true;
        listeners.submit(new Listener());

        for (int i=0; i<workerCount; i++)
            workers.submit(new AuthHandler());
    }

    public void stop(){
        if (authSocket == null)
            return;
        started = false;
        authSocket.close();
    }

    public void clearServlets(){
        servletMap.clear();
    }

    public void addServlet(InetAddress client, RadiusServlet servlet){
        if (client != null && servlet !=null)
            servletMap.put(client, servlet);
    }

    private DatagramSocket createAuthSocket(){
        int port = config.getAsInt("radius.authPort");
        String bindAddress = config.getAsStr("radius.authAddress");
        int timeout = config.getAsInt("radius.authListenerTimeout");

        try {
            InetAddress address = InetAddress.getByName(bindAddress);
            DatagramSocket socket = new DatagramSocket(port, address);
            socket.setSoTimeout(timeout);
            return socket;
        }
        catch (Throwable e){
            logService.warn("Can't create socket for RADIUS server", e);
            return null;
        }
    }

    private boolean offerAuthPacket(DatagramPacket packet){
        try {
            return requestAuthQueue.offer(packet);
        }
        catch (Throwable e){
            logService.warn("Can't add to server queue RADIUS auth packet", e);
            return false;
        }
    }

    private DatagramSocket getAuthSocket(){
        return authSocket;
    }

    private boolean isStarted(){
        return started;
    }

    private LogService getLogService(){
        return logService;
    }

    private DatagramPacket pullAuth(){
        try {
            return requestAuthQueue.poll(workerTimeout, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e){
            return null;
        }
    }

    private RadiusServlet getServlet(InetAddress remoteAddress){
        return servletMap.get(remoteAddress);
    }

    private void sendAnswer(DatagramPacket answer) throws IOException {
        authSocket.send(answer);
    }

}
