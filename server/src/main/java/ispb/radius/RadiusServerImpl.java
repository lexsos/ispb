package ispb.radius;

import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.fieldtype.RadiusClientType;
import ispb.base.radius.server.RadiusServer;
import ispb.base.radius.server.RadiusServlet;
import ispb.base.resources.Config;
import ispb.base.service.LogService;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class RadiusServerImpl implements RadiusServer {

    private final Config config;
    private final LogService logService;

    private volatile boolean started;
    private DatagramSocket authSocket;
    private DatagramSocket accountingSocket;
    private final BlockingQueue<PacketSocket> requestQueue;

    private final ExecutorService workers;
    private final int workerCount;
    private final ExecutorService listeners;

    private final Map<RadiusClientType, RadiusServlet> servletByType = new ConcurrentHashMap<>();
    private final Map<InetAddress, RadiusClientDataSet> clientByIp = new ConcurrentHashMap<>();

    public RadiusServerImpl(Config config, LogService logService){
        this.config = config;
        this.logService = logService;
        workerCount = config.getAsInt("radius.workerCount");

        int queueSize = config.getAsInt("radius.queueSize");
        requestQueue = new ArrayBlockingQueue<>(queueSize, true);
        workers = Executors.newFixedThreadPool(workerCount);
        listeners = Executors.newFixedThreadPool(2);
    }

    public void start(){
        authSocket = createAuthSocket();
        if (authSocket == null)
            return;

        accountingSocket = createAccountingSocket();
        if (accountingSocket == null)
            return;

        started = true;
        listeners.submit(new RadiusListener(authSocket, this, logService));
        listeners.submit(new RadiusListener(accountingSocket, this, logService));

        for (int i=0; i<workerCount; i++)
            workers.submit(new RadiusWorker(this, logService));
    }

    public void stop(){
        if (authSocket == null)
            return;
        started = false;
        authSocket.close();
        accountingSocket.close();
    }

    public void addServletType(RadiusClientType type, RadiusServlet servlet){
        servletByType.put(type, servlet);
    }

    public void loadRadiusClient(List<RadiusClientDataSet> clientList){
        clientByIp.clear();
        for (RadiusClientDataSet client: clientList)
            try {
                InetAddress ip4address = InetAddress.getByName(client.getIp4Address());
                clientByIp.put(ip4address, client);
            }
            catch (Throwable e){
                logService.warn("Can't add RADIUS client", e);
            }
    }

    public RadiusServlet getServletByClientIp(InetAddress address){
        RadiusClientDataSet client = clientByIp.get(address);
        if (client == null)
            return null;
        RadiusClientType clientType = client.getClientType();
        if (clientType == null)
            return null;
        return servletByType.get(clientType);
    }

    public RadiusClientDataSet getClientParameters(InetAddress address){
        return clientByIp.get(address);
    }

    public boolean isStarted(){
        return started;
    }

    public boolean offerPacket(PacketSocket packetSocket){
        try {
            return requestQueue.offer(packetSocket);
        }
        catch (Throwable e){
            logService.warn("Can't add to server queue RADIUS packet", e);
            return false;
        }
    }

    public PacketSocket pullPacket(){
        try {
            return requestQueue.take();
        }
        catch (InterruptedException e){
            return null;
        }
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
            logService.warn("Can't create auth socket for RADIUS server", e);
            return null;
        }
    }

    private DatagramSocket createAccountingSocket(){
        int port = config.getAsInt("radius.accountingPort");
        String bindAddress = config.getAsStr("radius.accountingAddress");
        int timeout = config.getAsInt("radius.authListenerTimeout");

        try {
            InetAddress address = InetAddress.getByName(bindAddress);
            DatagramSocket socket = new DatagramSocket(port, address);
            socket.setSoTimeout(timeout);
            return socket;
        }
        catch (Throwable e){
            logService.warn("Can't create accounting socket for RADIUS server", e);
            return null;
        }
    }
}
