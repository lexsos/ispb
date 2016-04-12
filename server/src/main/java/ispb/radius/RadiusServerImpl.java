package ispb.radius;

import ispb.base.radius.RadiusServer;
import ispb.base.resources.Config;
import ispb.base.service.LogService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;


public class RadiusServerImpl implements RadiusServer {

    private DatagramSocket authSocket = null;
    private final BlockingQueue<DatagramPacket> udpAuthQueue;
    private final Config config;
    private final LogService logService;
    private final int MAX_PACKET_LENGTH = 4096;
    private volatile boolean started;
    private final ExecutorService workers;
    private final ExecutorService listeners;
    private final int workerTimeout;
    private final int workerCount;

    private class Listener implements Runnable {

        public void run(){

            while (isStarted() && !getAuthSocket().isClosed()){
                DatagramPacket packetIn = new DatagramPacket(new byte[MAX_PACKET_LENGTH], MAX_PACKET_LENGTH);
                try {
                    getAuthSocket().receive(packetIn);
                    if (!offerAuthPacket(packetIn))
                        getLogService().info("Discard RADIUS auth packet from " + packetIn.getAddress());
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
                DatagramPacket packet = pullAuth();
                if (packet == null)
                    continue;
            }
        }
    }

    public RadiusServerImpl(Config config, LogService logService){
        this.config = config;
        this.logService = logService;
        workerTimeout = config.getAsInt("radius.workerTimeout");
        workerCount = config.getAsInt("radius.workerCount");
        int queueSize = config.getAsInt("radius.queueSize");

        udpAuthQueue = new ArrayBlockingQueue<>(queueSize, true);
        workers = Executors.newFixedThreadPool(workerCount);
        listeners = Executors.newFixedThreadPool(1);
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
            return udpAuthQueue.offer(packet);
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
            return udpAuthQueue.poll(workerTimeout, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e){
            return null;
        }
    }
}
