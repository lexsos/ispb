package ispb.eventsys;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.eventsys.EventSystem;
import ispb.base.resources.Config;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;


public class EventSystemImpl implements EventSystem {

    private final Application application;
    private final Queue<EventMessage> massageQueue;
    private final Map<Class, List<EventHandler>> handlers;
    private final Logger logger = Logger.getLogger(EventSystemImpl.class);
    private volatile boolean stopped;
    private final ExecutorService service;
    private final int poolSize;
    private final int sleepTime;

    private class Runner implements Runnable {

        public void run() {

            while (!isStopped()) {

                if (isEmpty())
                    try {
                        Thread.sleep(getSleepTime());
                    }
                    catch (InterruptedException ignored){

                    }

                try {
                    process();
                }
                catch (Throwable e){
                    logger.warn("Error in process thread event system", e);
                }
            }
        }
    }

    public EventSystemImpl(Application application, Config conf){
        stopped = false;
        this.application = application;
        massageQueue = new ConcurrentLinkedQueue<>();
        handlers = new ConcurrentHashMap<>();
        poolSize = conf.getAsInt("eventsys.poolsize");
        sleepTime = conf.getAsInt("eventsys.sleepTime");

        service = Executors.newFixedThreadPool(poolSize);
    }

    public void addHandler(EventHandler handler, Class eventType){
        if (!handlers.containsKey(eventType))
            handlers.put(eventType, new CopyOnWriteArrayList<>());
        List<EventHandler> handlerList = handlers.get(eventType);
        handlerList.add(handler);
    }

    public void pushMessage(EventMessage event){
        try {
            massageQueue.add(event);
        }
        catch (Throwable e){
            logger.warn("Can't add event message to queue", e);
        }
    }

    public void process(){
        if (stopped)
            return;

        EventMessage message = massageQueue.poll();
        if (message == null)
            return;

        Class eventType = message.getClass();
        if (!handlers.containsKey(eventType))
            return;

        List<EventHandler> handlerList = handlers.get(eventType);

        for (EventHandler handler : handlerList)
            try {
                handler.run(application, message);
            } catch (Throwable e) {
                logger.warn("Error in event message handler", e);
            }
    }

    public boolean isEmpty(){
        return massageQueue.isEmpty();
    }

    public boolean isStopped(){
        return stopped;
    }

    public void start(){
        for(int i = 0; i < poolSize; i++)
            service.execute(new Runner());
    }

    public void shutdownAndWait(){
        stopped = true;
        service.shutdown();

        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException e){
            logger.warn("(Interrupted while wait event message system thread pool ");
        }
    }

    private int getSleepTime() {
        return sleepTime;
    }
}
