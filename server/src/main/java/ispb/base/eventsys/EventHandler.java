package ispb.base.eventsys;


import ispb.base.Application;

public interface EventHandler {
    void run(Application application, EventMessage message);
}
