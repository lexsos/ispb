package ispb.radius.server;


import ispb.base.Application;
import ispb.base.radius.server.RadiusServer;
import ispb.base.radius.servlet.RadiusClientRepository;
import ispb.base.resources.Config;
import ispb.base.service.LogService;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.*;

public class RadiusServerImpl implements RadiusServer {

    private final Application application;

    private final Config config;
    private final LogService logService;

    private volatile boolean started = false;

    private RadiusClientRepository clientRepository;

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

    public void setClientRepository(RadiusClientRepository repository){
        clientRepository = repository;
    }

    public RadiusClientRepository getClientRepository(){
        return clientRepository;
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
