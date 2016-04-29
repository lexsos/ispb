package ispb.radius;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.radius.RadiusServer;
import ispb.base.radius.RadiusServlet;
import ispb.base.service.LogService;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RadiusWorker implements Runnable {

    private final RadiusServerImpl server;
    private final LogService logService;

    public RadiusWorker(RadiusServerImpl server, LogService logService){
        this.server = server;
        this.logService = logService;
    }

    public void run(){

        while (server.isStarted()){

            PacketSocket packetSocket = server.pullPacket();
            if (packetSocket == null)
                continue;

            DatagramPacket requestDatagram = packetSocket.getDatagram();
            DatagramSocket serverSocket = packetSocket.getServerSocket();
            InetAddress clientAddress = requestDatagram.getAddress();
            int clientPort = requestDatagram.getPort();

            RadiusServlet servlet = server.getServletByClientIp(clientAddress);
            if (servlet == null) {
                logService.info("Discard RADIUS packet, not found servlet for RADIUS client " + clientAddress);
                continue;
            }

            RadiusClientDataSet clientDataSet = server.getClientParameters(clientAddress);
            String sharedSecret = servlet.getSharedSecret(clientDataSet);
            if (sharedSecret == null || sharedSecret.length() == 0){
                logService.info("Discard RADIUS packet, shared secret is empty for " + clientAddress);
                continue;
            }

            RadiusPacket radiusRequest;

            try {
                radiusRequest = makeRadiusPacket(requestDatagram, sharedSecret);
            }
            catch (RadiusException e){
                logService.warn("Error while parse RADIUS packet from " + clientAddress, e);
                continue;
            }
            catch (IOException e){
                logService.warn("Can't read packet from " + clientAddress, e);
                continue;
            }


            RadiusPacket radiusResponse;
            try {
                radiusResponse = servlet.service(radiusRequest, clientDataSet);
            }
            catch (Throwable e){
                logService.warn("Error while execute RADIUS servlet for " + clientAddress, e);
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
                if (!serverSocket.isClosed())
                    serverSocket.send(answer);
                else
                    logService.warn("RADIUS server socket already closed");
            }
            catch (IOException e){
                logService.warn("Can't send packet to " + clientAddress, e);
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
