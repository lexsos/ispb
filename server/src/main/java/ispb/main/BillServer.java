package ispb.main;


import ispb.base.Application;
import ispb.base.eventsys.EventScheduler;
import ispb.base.eventsys.EventSystem;
import ispb.base.frontend.HttpServer;
import ispb.base.service.LogService;


public class BillServer {
    public static void run (String configFile) {

        Application application = BillingBuilder.build(configFile);

        LogService logService = application.getByType(LogService.class);
        EventSystem eventSystem = application.getByType(EventSystem.class);
        EventScheduler eventScheduler = application.getByType(EventScheduler.class);
        HttpServer server = application.getByType(HttpServer.class);

        logService.info("Starting http server");
        try {
            server.start();
        }
        catch (Exception e){
            logService.error("Can't start http server", e);
            System.exit(0);
        }

        logService.info("Starting event system");
        eventSystem.start();

        logService.info("Starting even scheduler");
        eventScheduler.start();

        try {
            server.join();
        }
        catch (InterruptedException e){
            logService.info("Join was interrupted", e);
        }
    }
}
