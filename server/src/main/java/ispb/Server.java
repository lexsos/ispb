package ispb;


import ispb.account.*;
import ispb.base.Application;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.eventsys.EventScheduler;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.handler.AddDailyPaymentHandler;
import ispb.base.eventsys.handler.CheckPaymentHandler;
import ispb.base.eventsys.message.AddDailyPaymentMsg;
import ispb.base.eventsys.message.CheckCustomerStatusMsg;
import ispb.base.eventsys.message.CheckPaymentMsg;
import ispb.base.eventsys.message.CheckTariffAssignmentMsg;
import ispb.base.frontend.HttpServer;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.DBService;
import ispb.base.service.LogService;
import ispb.base.service.account.*;
import ispb.base.service.dictionary.BuildingDictionaryService;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.dictionary.StreetDictionaryService;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.db.DBServiceImpl;
import ispb.db.util.QueryBuilderImpl;
import ispb.db.util.SortBuilderImpl;
import ispb.db.util.WhereBuilderImpl;
import ispb.dictionary.BuildingDictionaryServiceImpl;
import ispb.dictionary.CityDictionaryServiceImpl;
import ispb.dictionary.StreetDictionaryServiceImpl;
import ispb.dictionary.TariffDictionaryServiceImpl;
import ispb.eventsys.EventSchedulerImpl;
import ispb.eventsys.EventSystemImpl;
import ispb.base.eventsys.handler.CheckCustomerStatusHandler;
import ispb.base.eventsys.handler.CheckTariffAssignmentHandler;
import ispb.frontend.HttpServerImpl;
import ispb.log.LogServiceImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;


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
        resources.loadSingletons(application);
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

        CustomerAccountService customerAccountService = new CustomerAccountServiceImpl(daoFactory);
        application.addByType(CustomerAccountService.class, customerAccountService);

        CustomerStatusService customerStatusService = new CustomerStatusServiceImpl(daoFactory, application);
        application.addByType(CustomerStatusService.class, customerStatusService);

        TariffDictionaryService tariffDictionaryService = new TariffDictionaryServiceImpl(daoFactory);
        application.addByType(TariffDictionaryService.class, tariffDictionaryService);

        PaymentService paymentService = new PaymentServiceImpl(daoFactory, application);
        application.addByType(PaymentService.class, paymentService);

        TariffAssignmentService tariffAssignmentService = new TariffAssignmentServiceImpl(daoFactory, application);
        application.addByType(TariffAssignmentService.class, tariffAssignmentService);

        EventSystem eventSystem = new EventSystemImpl(application, conf);
        application.addByType(EventSystem.class, eventSystem);

        TariffPolicyService tariffPolicyService = new TariffPolicyServiceImpl(application);
        application.addByType(TariffPolicyService.class, tariffPolicyService);

        logService.info("Starting http server");
        HttpServer server = new HttpServerImpl(conf);
        application.addByType(HttpServer.class, server);
        try {
            server.start();
        }
        catch (Exception e){
            logService.error("Can't start http server", e);
            System.exit(0);
        }

        eventSystem.addHandler(new CheckTariffAssignmentHandler(), CheckTariffAssignmentMsg.class);
        eventSystem.addHandler(new CheckCustomerStatusHandler(), CheckCustomerStatusMsg.class);
        eventSystem.addHandler(new CheckPaymentHandler(), CheckPaymentMsg.class);
        eventSystem.addHandler(new AddDailyPaymentHandler(), AddDailyPaymentMsg.class);

        logService.info("Starting event system");
        eventSystem.start();

        EventScheduler eventScheduler = new EventSchedulerImpl(logService, application);
        application.addByType(EventScheduler.class, eventScheduler);
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
