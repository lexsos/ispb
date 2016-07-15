package ispb.radius.server;


import ispb.base.radius.packet.RadiusPacket;
import ispb.base.service.LogService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

public class RadiusListener implements Runnable {

    private final DatagramSocket serverSocket;
    private final LogService logService;
    private final BlockingQueue<PacketSocket> requestQueue;

    public RadiusListener(DatagramSocket serverSocket, LogService logService, BlockingQueue<PacketSocket> requestQueue){
        this.serverSocket = serverSocket;
        this.logService = logService;
        this.requestQueue = requestQueue;
    }

    public void run(){

        while (!serverSocket.isClosed()){

            DatagramPacket datagram = new DatagramPacket(new byte[RadiusPacket.MAX_LENGTH], RadiusPacket.MAX_LENGTH);

            try {
                serverSocket.receive(datagram);
                PacketSocket packetSocket = new PacketSocket(datagram, serverSocket);
                try {
                    requestQueue.offer(packetSocket);
                    logService.debug("Receive RADIUS packet from " + datagram.getAddress());
                }
                catch (Throwable e){
                    logService.info("Discard RADIUS auth packet from " + datagram.getAddress());
                }
            }
            catch (SocketTimeoutException ignored){
            }
            catch (Throwable e){
                logService.warn("Interrupted while wait RADIUS auth packet", e);
            }
        }
    }
}
