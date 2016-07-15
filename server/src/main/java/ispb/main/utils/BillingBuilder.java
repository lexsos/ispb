package ispb.main.utils;


import ispb.ApplicationImpl;
import ispb.account.*;
import ispb.base.Application;
import ispb.base.db.fieldtype.RadiusClientType;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.eventsys.message.*;
import ispb.base.radius.server.RadiusAttributeListBuilder;
import ispb.base.radius.auth.RadiusAuthProcessor;
import ispb.base.radius.auth.RadiusChapAuth;
import ispb.base.radius.auth.RadiusPapAuth;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.dictionary.RadiusDictionaryReader;
import ispb.base.radius.dictionary.RadiusDictionaryReaderImpl;
import ispb.base.radius.dictionary.RadiusMemoryDictionary;
import ispb.base.radius.io.RadiusPacketReader;
import ispb.base.radius.io.RadiusPacketSerializer;
import ispb.base.radius.io.RadiusPacketWriter;
import ispb.base.radius.middleware.RadiusMiddleProcessor;
import ispb.base.radius.middleware.RadiusProxyStateMiddle;
import ispb.base.radius.middleware.RadiusResponseAuthenticatorMiddle;
import ispb.base.radius.middleware.RadiusUserPasswordMiddle;
import ispb.base.radius.server.RadiusServer;
import ispb.base.scheduler.EventScheduler;
import ispb.base.eventsys.EventSystem;
import ispb.base.service.dictionary.*;
import ispb.dictionary.*;
import ispb.eventsys.handler.*;
import ispb.base.frontend.HttpServer;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.DBService;
import ispb.base.service.LogService;
import ispb.base.service.account.*;
import ispb.db.DBServiceImpl;
import ispb.db.util.QueryBuilderImpl;
import ispb.db.util.SortBuilderImpl;
import ispb.db.util.WhereBuilderImpl;
import ispb.radius.server.RadiusAttributeListBuilderImpl;
import ispb.radius.server.RadiusServerImpl;
import ispb.radius.servlet.DefaultRadiusServlet;
import ispb.scheduler.EventSchedulerImpl;
import ispb.eventsys.EventSystemImpl;
import ispb.frontend.HttpServerImpl;
import ispb.log.LogServiceImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;


public class BillingBuilder {

    private static void buildRadius(Application application){
        LogService logService = application.getByType(LogService.class);
        Config conf = application.getByType(Config.class);
        RadiusClientDictionaryService radiusClientDictionaryService = application.getByType(RadiusClientDictionaryService.class);
        CustomerAccountService customerAccountService =  application.getByType(CustomerAccountService.class);
        TariffDictionaryService tariffDictionaryService = application.getByType(TariffDictionaryService.class);
        RadiusUserService radiusUserService = application.getByType(RadiusUserService.class);

        RadiusDictionaryReader radiusDictionaryReader = new RadiusDictionaryReaderImpl(logService);
        application.addByType(RadiusDictionaryReader.class, radiusDictionaryReader);

        RadiusDictionary radiusDictionary = new RadiusMemoryDictionary();
        radiusDictionaryReader.readDefaultDictionary(radiusDictionary);
        application.addByType(RadiusDictionary.class, radiusDictionary);

        String radiusDictionaryFile = conf.getAsStr("radius.dictionaryFile");
        if (radiusDictionaryFile != null)
            radiusDictionaryReader.readDictionary(radiusDictionaryFile, radiusDictionary);

        RadiusPacketSerializer serializer = new RadiusPacketSerializer(radiusDictionary);
        application.addByType(RadiusPacketReader.class, serializer);
        application.addByType(RadiusPacketWriter.class, serializer);

        RadiusMiddleProcessor middleProcessor = new RadiusMiddleProcessor();
        middleProcessor.addIn(new RadiusUserPasswordMiddle());
        // TODO: add  middleware for Vendor Attributes
        middleProcessor.addOut(new RadiusUserPasswordMiddle());
        middleProcessor.addOut(new RadiusProxyStateMiddle());
        middleProcessor.addOut(new RadiusResponseAuthenticatorMiddle());
        application.addByType(RadiusMiddleProcessor.class, middleProcessor);

        RadiusAuthProcessor authProcessor = new  RadiusAuthProcessor();
        authProcessor.addAuthMethod(new RadiusPapAuth());
        authProcessor.addAuthMethod(new RadiusChapAuth());
        application.addByType(RadiusAuthProcessor.class, authProcessor);

        RadiusAttributeListBuilder radiusAttributeListBuilder = new RadiusAttributeListBuilderImpl(
                customerAccountService,
                tariffDictionaryService,
                radiusUserService);
        application.addByType(RadiusAttributeListBuilder.class, radiusAttributeListBuilder);

        RadiusServer radiusServer = new RadiusServerImpl(application);
        application.addByType(RadiusServer.class, radiusServer);

        radiusServer.addServletType(RadiusClientType.DEFAULT, new DefaultRadiusServlet(conf));

        radiusServer.loadRadiusClient(radiusClientDictionaryService.getRadiusClientList());
    }

    public static Application build(String configFile){
        Application application = ApplicationImpl.getApplication();

        Config conf = new ConfigImpl(configFile );
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
        application.addByType(DBService.class, dbService);
        dbService.migrate();

        DaoFactory daoFactory = dbService.getDaoFactory();
        application.addByType(DaoFactory.class, daoFactory);

        UserAccountService userAccountService = new UserAccountServiceImpl(daoFactory, logService);
        application.addByType(UserAccountService.class, userAccountService);

        CityDictionaryService cityDictionaryService = new CityDictionaryServiceImpl(daoFactory);
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

        TariffPolicyService tariffPolicyService = new TariffPolicyServiceImpl(application, daoFactory);
        application.addByType(TariffPolicyService.class, tariffPolicyService);

        RadiusUserService radiusUserService = new RadiusUserServiceImpl(daoFactory, application);
        application.addByType(RadiusUserService.class, radiusUserService);

        RadiusClientDictionaryService radiusClientDictionaryService = new RadiusClientDictionaryServiceImpl(daoFactory, application);
        application.addByType(RadiusClientDictionaryService.class, radiusClientDictionaryService);

        EventSystem eventSystem = new EventSystemImpl(application, conf);
        application.addByType(EventSystem.class, eventSystem);

        eventSystem.addHandler(new CheckTariffAssignmentHandler(), CheckTariffAssignmentMsg.class);
        eventSystem.addHandler(new CheckCustomerStatusHandler(), CheckCustomerStatusMsg.class);
        eventSystem.addHandler(new CheckPaymentHandler(), CheckPaymentMsg.class);
        eventSystem.addHandler(new AddDailyPaymentHandler(), AddDailyPaymentMsg.class);
        eventSystem.addHandler(new LoadRadiusClientHandler(), RadiusClientUpdatedMsq.class);

        EventScheduler eventScheduler = new EventSchedulerImpl(logService, application);
        application.addByType(EventScheduler.class, eventScheduler);

        HttpServer server = new HttpServerImpl(conf);
        application.addByType(HttpServer.class, server);

        /*String radiusDictionaryFile = conf.getAsStr("radius.dictionaryFile");
        try {
            if (radiusDictionaryFile != null)
                DefaultRadiusDictionary.addFromFile(radiusDictionaryFile);
        }
        catch (IOException e){
            logService.warn(e.getMessage(), e);
        }*/

        //WritableRadiusDictionary radiusDictionary = DefaultRadiusDictionary.getDefaultDictionary();
        //application.addByType(WritableRadiusDictionary.class, radiusDictionary);

        //RadiusServer radiusServer = new RadiusServerImpl(conf, logService);
        //application.addByType(RadiusServer.class, radiusServer);

        //radiusServer.loadRadiusClient(radiusClientDictionaryService.getRadiusClientList());



        /*DefaultRadiusServlet defaultRadiusServlet = new DefaultRadiusServlet(
                logService,
                radiusAttributeListBuilder,
                radiusUserService,
                customerAccountService,
                conf);
        radiusServer.addServletType(RadiusClientType.DEFAULT, defaultRadiusServlet);*/

        buildRadius(application);

        return application;
    }
}
