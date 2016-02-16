package ispb;


import ispb.base.Application;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.frontend.HttpServer;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.DBService;
import ispb.base.service.LogService;
import ispb.base.service.UserAccountService;
import ispb.base.service.dictionary.BuildingDictionaryService;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.dictionary.StreetDictionaryService;
import ispb.db.DBServiceImpl;
import ispb.db.util.QueryBuilderImpl;
import ispb.db.util.SortBuilderImpl;
import ispb.db.util.WhereBuilderImpl;
import ispb.dictionary.BuildingDictionaryServiceImpl;
import ispb.dictionary.CityDictionaryServiceImpl;
import ispb.dictionary.StreetDictionaryServiceImpl;
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

        WhereBuilder whereBuilder = new WhereBuilderImpl();
        SortBuilder sortBuilder = new SortBuilderImpl();
        QueryBuilder queryBuilder = new QueryBuilderImpl(whereBuilder, sortBuilder);

        DBService dbService = new DBServiceImpl(conf, resources, logService, queryBuilder);
        dbService.migrate();

        DaoFactory daoFactory = dbService.getDaoFactory();
        application.addByType(DaoFactory.class, daoFactory);

        UserAccountService userAccountService = new UserAccountServiceImpl(daoFactory);
        application.addByType(UserAccountService.class, userAccountService);

        CityDictionaryService  cityDictionaryService = new CityDictionaryServiceImpl(daoFactory);
        application.addByType(CityDictionaryService.class, cityDictionaryService);

        StreetDictionaryService streetDictionaryService = new StreetDictionaryServiceImpl(daoFactory);
        application.addByType(StreetDictionaryService.class, streetDictionaryService);

        BuildingDictionaryService buildingDictionaryService = new BuildingDictionaryServiceImpl(daoFactory);
        application.addByType(BuildingDictionaryService.class, buildingDictionaryService);

        logService.info("Starting http server");
        HttpServer server = new HttpServerImpl(conf);
        try {
            server.start();
        }
        catch (Exception e){
            logService.error("Can't start http server", e);
            System.exit(0);
        }

        try {
            server.join();
        }
        catch (InterruptedException e){
            logService.info("Join was interrupted", e);
        }

    }
}
