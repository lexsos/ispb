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
        application.setConfig(conf);

        AppResources resources = AppResourcesImpl.getInstance();
        application.setAppResources(resources);

        LogService logService  = new LogServiceImpl(conf);
        application.setLogService(logService);

        DBService dbService = new DBServiceImpl(application);
        dbService.migrate();

        DaoFactory daoFactory = dbService.getDaoFactory();
        application.setDaoFactory(daoFactory);

        UserAccountService userAccountService = new UserAccountServiceImpl(application);
        application.setUserAccountService(userAccountService);

        application.getLogService().info("Starting http server");
        HttpServer server = new HttpServerImpl(application);
        try {
            server.start();
        }
        catch (Exception e){
            application.getLogService().error("Can't start http server", e);
            System.exit(0);
        }

        try {
            server.join();
        }
        catch (InterruptedException e){
            application.getLogService().info("Join was interrupted", e);
        }

    }
}
