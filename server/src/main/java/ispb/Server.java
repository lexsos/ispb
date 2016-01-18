package ispb;


import ispb.base.Application;
import ispb.base.db.utils.DaoFactory;
import ispb.base.frontend.HttpServer;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.DBService;
import ispb.base.service.LogService;
import ispb.base.service.UserAccountService;
import ispb.db.DBServiceImpl;
import ispb.frontend.HttpServerImpl;
import ispb.log.LogServiceImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;
import ispb.users.UserAccountServiceImpl;


public class Server {
    public static void main( String[] args ){
        if (args.length != 1) {
            System.out.println("Need config file");
            System.exit(0);
        }

        Application application = ApplicationImpl.getApplication();

        Config conf = new ConfigImpl( args[0] );
        application.addByType(Config.class, conf);

        AppResources resources = AppResourcesImpl.getInstance();
        application.addByType(AppResources.class, resources);

        LogService logService  = new LogServiceImpl(conf);
        application.addByType(LogService.class, logService);

        DBService dbService = new DBServiceImpl(application);
        dbService.migrate();

        DaoFactory daoFactory = dbService.getDaoFactory();
        application.addByType(DaoFactory.class, daoFactory);

        UserAccountService userAccountService = new UserAccountServiceImpl(application);
        application.addByType(UserAccountService.class, userAccountService);

        application.getByType(LogService.class).info("Starting http server");
        HttpServer server = new HttpServerImpl(application);
        try {
            server.start();
        }
        catch (Exception e){
            application.getByType(LogService.class).error("Can't start http server", e);
            System.exit(0);
        }

        try {
            server.join();
        }
        catch (InterruptedException e){
            application.getByType(LogService.class).info("Join was interrupted", e);
        }

    }
}
