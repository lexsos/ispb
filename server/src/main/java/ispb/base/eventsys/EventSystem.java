package ispb.base.eventsys;


public interface EventSystem {
    void addHandler(EventHandler handler, Class eventType);
    void pushMessage(EventMessage event);
    void process();
    boolean isEmpty();
    boolean isStopped();
    void start();
    void shutdownAndWait();
}
