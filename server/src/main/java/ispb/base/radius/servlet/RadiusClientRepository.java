package ispb.base.radius.servlet;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.service.LogService;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RadiusClientRepository {

    private static class RadiusClient {

        private final RadiusClientDataSet client;
        private final RadiusServlet servlet;

        public RadiusClient(RadiusClientDataSet client, RadiusServlet servlet){
            this.client = client;
            this.servlet = servlet;
        }

        public RadiusClientDataSet getClient() {
            return client;
        }

        public RadiusServlet getServlet() {
            return servlet;
        }
    }

    private final Map<InetAddress, RadiusClient> clients = new  ConcurrentHashMap<>();
    private final LogService logService;

    public RadiusClientRepository(LogService logService){
        this.logService = logService;
    }

    public void addClient(RadiusClientDataSet client, RadiusServlet servlet){
        try {
            InetAddress address = InetAddress.getByName(client.getIp4Address());
            RadiusClient radiusClient = new RadiusClient(client, servlet);
            clients.put(address, radiusClient);
        }
        catch (Throwable e){
            logService.warn("Can't add servlet to RADIUS servlet repository for client: " + client.getIp4Address(), e);
        }
    }

    public RadiusServlet getServlet(InetAddress clientAddress){
        if (clients.containsKey(clientAddress))
            return clients.get(clientAddress).getServlet();
        return null;
    }

    public RadiusServlet getServlet(String clientAddress){
        try {
            InetAddress address = InetAddress.getByName(clientAddress);
            return getServlet(address);
        }
        catch (Throwable e){
            logService.warn("Can't extract servlet from RADIUS servlet repository for client: " + clientAddress, e);
        }
        return null;
    }

    public RadiusClientDataSet getClient(InetAddress clientAddress){
        if (clients.containsKey(clientAddress))
            return clients.get(clientAddress).getClient();
        return null;
    }

    public RadiusClientDataSet getClient(String clientAddress){
        try {
            InetAddress address = InetAddress.getByName(clientAddress);
            return getClient(address);
        }
        catch (Throwable e){
            logService.warn("Can't extract RADIUS client info for ip: " + clientAddress, e);
        }
        return null;
    }
}
