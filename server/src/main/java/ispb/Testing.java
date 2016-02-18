package ispb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import ispb.account.CustomerAccountServiceImpl;
import ispb.base.Application;
import ispb.base.db.dao.*;
import ispb.base.db.dataset.*;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.*;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.sort.SortBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.frontend.HttpServer;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.UserAccountService;
import ispb.base.service.dictionary.BuildingDictionaryService;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.dictionary.StreetDictionaryService;
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
import ispb.account.UserAccountServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import ispb.base.service.DBService;
import ispb.db.DBServiceImpl;


public class Testing
{
    public static class JsonTest {

        private Map<String, String> params = new HashMap<String, String>();

        public static JsonTest fromJson(String jsonData){
            Gson gson = new Gson();
            return gson.fromJson(jsonData, JsonTest.class);
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

        public String toJson() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public static void main( String[] args ) throws Exception
    {
        JsonTest jsonTest = new JsonTest();
        jsonTest.getParams().put("1", "2");
        jsonTest.getParams().put("3", "4");
        String jsonData = jsonTest.toJson();
        System.out.println(jsonData);

        JsonTest jsonTest2 = JsonTest.fromJson(jsonData);

        Application application = ApplicationImpl.getApplication();
        Config conf = new ConfigImpl( args[0] );
        application.addByType(Config.class, conf);
        AppResources resources = AppResourcesImpl.getInstance();
        application.addByType(AppResources.class, resources);

        LogService logService = new LogServiceImpl(conf);
        application.addByType(LogService.class, logService);


        WhereBuilder whereBuilder = new WhereBuilderImpl();
        SortBuilder sortBuilder = new SortBuilderImpl();
        QueryBuilder queryBuilder = new QueryBuilderImpl(whereBuilder, sortBuilder);

        DBService dbSrv = new DBServiceImpl(conf, resources, logService, queryBuilder);
        dbSrv.clearDB();
        dbSrv.migrate();
        DaoFactory daoFactory = dbSrv.getDaoFactory();
        application.addByType(DaoFactory.class, daoFactory);

        System.out.println(DBService.class.getTypeName());
        System.out.println(DBService.class.getCanonicalName());
        System.out.println(DBService.class.getName());
        System.out.println(DBService.class.getSimpleName());
        application.addByType(DBService.class, dbSrv);
        DBService dbSrv1 = application.getByType(DBService.class);

        CityDataSet c = new CityDataSet("Vologda");
        application.getByType(DaoFactory.class).getCityDao().save(c);
        System.out.println(c);

        StreetDataSet st = new StreetDataSet("Mira", c);
        application.getByType(DaoFactory.class).getStreetDao().save(st);

        BuildingDataSet b = new BuildingDataSet("1A", st);
        application.getByType(DaoFactory.class).getBuildingDao().save(b);

        b = new BuildingDataSet("2", st);
        application.getByType(DaoFactory.class).getBuildingDao().save(b);

        b = new BuildingDataSet("3", st);
        application.getByType(DaoFactory.class).getBuildingDao().save(b);

        c.setName("Sokol");
        application.getByType(DaoFactory.class).getCityDao().save(c);


        CustomerDataSet cus = new CustomerDataSet();
        cus.setName("user");
        cus.setContractNumber("c0000111");
        cus.setBuilding(b);

        application.getByType(DaoFactory.class).getCustomerDao().save(cus);

        for (Iterator iterator = application.getByType(DaoFactory.class).getBuildingDao().getAll().iterator(); iterator.hasNext();){
            BuildingDataSet building = (BuildingDataSet) iterator.next();
            System.out.println( building );
        }

        CityDataSetDao CityDao = application.getByType(DaoFactory.class).getCityDao();

        System.out.println( CityDao.save(new CityDataSet("Vologda")) );
        System.out.println( CityDao.save(new CityDataSet("Moscow")) );
        System.out.println( CityDao.save(new CityDataSet("Spb")) );
        CityDao.delete( CityDao.getById(4) );
        CityDao.getById(10);
        System.out.println( CityDao.getAll() );
        System.out.println( CityDao.getByName("Vologda") );
        System.out.println( CityDao.getByName("Spb") );

        StreetDataSetDao StreetDao = application.getByType(DaoFactory.class).getStreetDao();
        StreetDao.save(new StreetDataSet("Koneva", c));
        StreetDao.save(new StreetDataSet("Kirova", c));
        System.out.println( StreetDao.getAll() );
        System.out.println( StreetDao.getByCity(c) );

        BuildingDataSetDao buildingDao = application.getByType(DaoFactory.class).getBuildingDao();
        System.out.println( buildingDao.getAll() );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByCity(c) );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByStreet(st) );

        System.out.println( buildingDao.getById(1) );


        DaoFactory daoF = application.getByType(DaoFactory.class);

        CustomerDataSetDao customerDao = daoF.getCustomerDao();
        CustomerDataSet customer = new CustomerDataSet();
        customer.setName("alex");
        customer.setBuilding(b);
        customer.setContractNumber("123");
        customer.setPhone("+7(921)12345678");
        customerDao.save(customer);

        for (int i=0; i<100; i++){
            customer = new CustomerDataSet();
            customer.setName("alex" + i);
            customer.setBuilding(b);
            customer.setContractNumber("num" + i);
            customer.setPhone("+7(921)12345679");
            customerDao.save(customer);
        }

        System.out.println(conf.getAsStr("db.host"));
        System.out.println(conf.getAsStr("db.port"));
        System.out.println(conf.getAsStr("db.base"));
        System.out.println(conf.getAsStr("db.user"));
        System.out.println(conf.getAsStr("db.passwd"));

        UserDataSet user = new UserDataSet();
        user.setLogin("alex");
        user.setPassword(DigestUtils.sha1Hex("abc123456"));
        user.setSalt("abc");
        user.setAccessLevel(AccessLevel.ADMIN);
        user.setName("alexander");
        user.setSurname("------");
        System.out.println(daoF.getUserDao().save(user));

        System.out.println(user);
        System.out.println(daoF.getUserDao().getAll());
        System.out.println(daoF.getUserDao().getByLogin("alex"));
        System.out.println(daoF.getUserDao().getByLogin("lex"));

        UserAccountService userAccountService = new UserAccountServiceImpl(daoFactory);
        application.addByType(UserAccountService.class, userAccountService);

        UserAccountService accSer = new UserAccountServiceImpl(daoFactory);
        System.out.println(accSer.addUser("lex", "123456", "Lex", "---", AccessLevel.ADMIN));

        CityDictionaryService cityDictionaryService = new CityDictionaryServiceImpl(daoF);
        application.addByType(CityDictionaryService.class, cityDictionaryService);

        cityDictionaryService.update(c.getId(), c.getName());

        BuildingDictionaryService buildingDictionaryService = new BuildingDictionaryServiceImpl(daoF);
        application.addByType(BuildingDictionaryService.class, buildingDictionaryService);

        StreetDictionaryService streetDictionaryService = new StreetDictionaryServiceImpl(daoFactory);
        application.addByType(StreetDictionaryService.class, streetDictionaryService);

        CustomerAccountService customerAccountService = new CustomerAccountServiceImpl(daoFactory);
        application.addByType(CustomerAccountService.class, customerAccountService);

        CustomerSummeryViewDao cusSum = daoFactory.getCustomerSummeryViewDao();
        DataSetFilter filter = new DataSetFilter();
        DataSetSort sort = new DataSetSort();
        Pagination pagination = new Pagination();
        System.out.println( cusSum.getList(filter, sort, pagination) );
        System.out.println( cusSum.getCount(filter) );

        filter.add("contractNumber", CmpOperator.EQ, "c0000111");
        System.out.println( cusSum.getList(filter, sort, pagination) );
        System.out.println( cusSum.getCount(filter) );


        application.getByType(LogService.class).info("Starting http server");

        HttpServer server = new HttpServerImpl(conf);
        server.start();
        server.join();

        System.out.println( "Hello World!" );
        System.exit(0);
    }
}
