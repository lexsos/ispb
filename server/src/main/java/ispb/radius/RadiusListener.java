package ispb.radius;


import ispb.base.service.LogService;
import org.tinyradius.packet.RadiusPacket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class RadiusListener implements Runnable {

    private final DatagramSocket serverSocket;
    private final LogService logService;
    private final RadiusServerImpl server;

    public RadiusListener(DatagramSocket serverSocket, RadiusServerImpl server, LogService logService) {
        this.serverSocket = serverSocket;
        this.server = server;
        this.logService = logService;
    }

    public void run(){

        while (!serverSocket.isClosed() && server.isStarted()){

            DatagramPacket datagram = new DatagramPacket(new byte[RadiusPacket.MAX_PACKET_LENGTH], RadiusPacket.MAX_PACKET_LENGTH);

            try {
                serverSocket.receive(datagram);

                PacketSocket packetSocket = new PacketSocket(datagram, serverSocket);

                if (server.offerPacket(packetSocket))
                    logService.debug("Receive RADIUS packet from " + datagram.getAddress());
                else
                    logService.info("Discard RADIUS auth packet from " + datagram.getAddress());
            }
            catch (SocketTimeoutException ignored){
            }
            catch (Throwable e){
                logService.warn("Interrupted while wait RADIUS auth packet", e);
            }
        }
    }
}


