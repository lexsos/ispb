package ispb.radius.server;


import ispb.base.Application;
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

    private final Application application;

    private final Config config;
    private final LogService logService;

    private volatile boolean started = false;

    private final Map<RadiusClientType, RadiusServlet> servletMap = new ConcurrentHashMap<>();
    private final Map<InetAddress, RadiusClientDataSet> clientMap = new ConcurrentHashMap<>();

    private final List<DatagramSocket> serverSocketList = new CopyOnWriteArrayList<>();

    private final BlockingQueue<PacketSocket> requestQueue;

    public RadiusServerImpl(Application application){

        this.application = application;

        config = application.getByType(Config.class);
        logService = application.getByType(LogService.class);

        requestQueue = new ArrayBlockingQueue<>(config.getAsInt("radius.queueSize"), true);
    }

    public void start(){
        if (started)
            logService.warn("RADIUS server already started");

        if (!initSocketList()){
            logService.warn("Can't start RADIUS server. There are not server sockets");
            return;
        }
        started = true;

        int workerCount = config.getAsInt("radius.workerCount");
        ExecutorService workers = Executors.newFixedThreadPool(serverSocketList.size() + workerCount);
        for (DatagramSocket socket: serverSocketList)
            workers.submit(new RadiusListener(socket, logService, requestQueue));
        for (int i = 0; i < workerCount; i++)
            workers.submit(new RadiusWorker(this, requestQueue, application));
    }

    public void stop(){
        if (!started)
            logService.warn("RADIUS server not started");

        closeSocketList();
        started = false;
    }

    public boolean isStarted(){
        return started;
    }

    public void addServletType(RadiusClientType type, RadiusServlet servlet){
        servletMap.put(type, servlet);
    }

    public void loadRadiusClient(List<RadiusClientDataSet> clientList){
        clientMap.clear();
        for (RadiusClientDataSet client: clientList)
            try {
                InetAddress ip4address = InetAddress.getByName(client.getIp4Address());
                clientMap.put(ip4address, client);
            }
            catch (Throwable e){
                logService.warn("Can't add RADIUS client", e);
            }
    }

    public RadiusServlet getServlet(InetAddress address){
        RadiusClientDataSet client = clientMap.get(address);
        if (client == null)
            return null;
        RadiusClientType clientType = client.getClientType();
        if (clientType == null)
            return null;
        return servletMap.get(clientType);
    }

    public RadiusClientDataSet getClient(InetAddress address){
        return clientMap.get(address);
    }


    private boolean initSocketList(){
        String[] bindList = config.getAsStr("radius.bindAddress").split(";");
        int listenerTimeout = config.getAsInt("radius.listenerTimeout");
        for (String bind: bindList){
            try {
                String address = bind.split(":")[0];
                int port = Integer.parseInt(bind.split(":")[1]);

                InetAddress inetAddress = InetAddress.getByName(address);
                DatagramSocket socket = new DatagramSocket(port, inetAddress);
                socket.setSoTimeout(listenerTimeout);

                serverSocketList.add(socket);
            }
            catch (Throwable e){
                logService.warn("Can't create RADIUS server socket for address " + bind);
            }
        }
        return serverSocketList.size() > 0;
    }

    @SuppressWarnings("Convert2streamapi")
    private void closeSocketList(){
        for (DatagramSocket socket: serverSocketList)
            socket.close();
        serverSocketList.clear();
    }
}
