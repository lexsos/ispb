package ispb.radius.server;


import ispb.base.Application;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.radius.RadiusAttributeList;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.exception.RadiusPacketInvalidLength;
import ispb.base.radius.io.RadiusPacketReader;
import ispb.base.radius.io.RadiusPacketWriter;
import ispb.base.radius.middleware.RadiusMiddleProcessor;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.server.RadiusServlet;
import ispb.base.radius.server.RadiusServletContext;
import ispb.base.service.LogService;
import ispb.base.utils.HexCodec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public class RadiusWorker implements Runnable {

    private final RadiusServerImpl server;
    private final BlockingQueue<PacketSocket> requestQueue;
    private final Application application;
    private final LogService logService;
    private final RadiusMiddleProcessor middleProcessor;
    private final RadiusPacketReader reader;
    private final RadiusPacketWriter writer;


    public RadiusWorker(RadiusServerImpl server,
                        BlockingQueue<PacketSocket> requestQueue,
                        Application application){
        this.server = server;
        this.requestQueue = requestQueue;
        this.application = application;

        logService = application.getByType(LogService.class);
        middleProcessor = application.getByType(RadiusMiddleProcessor.class);
        reader = application.getByType(RadiusPacketReader.class);
        writer = application.getByType(RadiusPacketWriter.class);
    }

    public void run(){
        while (server.isStarted()){
            PacketSocket packetSocket;
            try {
                packetSocket = requestQueue.take();
            }
            catch (InterruptedException e){
                continue;
            }
            if (packetSocket == null)
                continue;

            DatagramPacket requestDatagram = packetSocket.getDatagram();
            DatagramSocket serverSocket = packetSocket.getServerSocket();
            InetAddress clientAddress = requestDatagram.getAddress();
            int clientPort = requestDatagram.getPort();

            RadiusServlet servlet = server.getServlet(clientAddress);
            if (servlet == null) {
                logService.info("Discard RADIUS packet, not found servlet for RADIUS client " + clientAddress);
                continue;
            }

            RadiusClientDataSet client = server.getClient(clientAddress);
            String sharedSecret = servlet.getSharedSecret(client);
            if (sharedSecret == null || sharedSecret.length() == 0){
                logService.info("Discard RADIUS packet, shared secret is empty for " + clientAddress);
                continue;
            }

            RadiusPacket request;
            try {
                request = reader.read(requestDatagram.getData());
            }
            catch (RadiusPacketInvalidLength e){
                logService.info("Discard RADIUS packet, invalid data length for " + clientAddress);
                continue;
            }

            try {
                middleProcessor.in(request, HexCodec.stringToByte(sharedSecret));
            }
            catch (RadiusException e){
                logService.info("Discard RADIUS packet, error occurred in incoming middleware for client " + clientAddress, e);
                continue;
            }

            RadiusPacket response;
            RadiusServletContext context = new RadiusServletContext();
            context.setApplication(application);
            context.setRequest(request);
            context.setClient(client);
            context.setLogService(logService);
            context.setAttributeList(new RadiusAttributeList(request));
            try {
                response = servlet.service(context);
            }
            catch (Throwable e)
            {
                logService.warn("Error while execute RADIUS servlet for " + clientAddress, e);
                continue;
            }
            if (response == null) {
                logService.info("Discard RADIUS packet, no response from servlet for client " + clientAddress);
                continue;
            }

            try {
                middleProcessor.out(request, response, HexCodec.stringToByte(sharedSecret));
            }
            catch (RadiusException e){
                logService.info("Error while execute RADIUS outgoing middleware for client " + clientAddress, e);
                continue;
            }

            byte[] data = writer.write(response);
            DatagramPacket answer = new  DatagramPacket(data, data.length, clientAddress, clientPort);
            try {
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
}
