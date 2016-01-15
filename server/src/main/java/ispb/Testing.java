package ispb;

import java.util.Iterator;
import ispb.base.Application;
import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.*;
import ispb.base.db.utils.DaoFactory;
import ispb.base.frontend.HttpServer;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.UserAccountService;
import ispb.frontend.HttpServerImpl;
import ispb.log.LogServiceImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;
import ispb.users.UserAccountServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import ispb.base.service.DBService;
import ispb.db.DBServiceImpl;


public class Testing
{
    public static void main( String[] args ) throws Exception
    {
        Application application = ApplicationImpl.getApplication();
        Config conf = new ConfigImpl( args[0] );
        application.setConfig(conf);
        application.addByType(Config.class, conf);
        application.setAppResources(AppResourcesImpl.getInstance());
        application.addByType(AppResources.class, AppResourcesImpl.getInstance());

        LogService logService = new LogServiceImpl(conf);
        application.setLogService(logService);
        application.addByType(LogService.class, logService);


        DBService dbSrv = new DBServiceImpl(application);
        dbSrv.clearDB();
        dbSrv.migrate();
        DaoFactory daoFactory = dbSrv.getDaoFactory();
        application.setDaoFactory(daoFactory);
        application.addByType(DaoFactory.class, daoFactory);

        System.out.println(DBService.class.getTypeName());
        System.out.println(DBService.class.getCanonicalName());
        System.out.println(DBService.class.getName());
        System.out.println(DBService.class.getSimpleName());
        application.addByType(DBService.class, dbSrv);
        DBService dbSrv1 = application.getByType(DBService.class);

        CityDataSet c = new CityDataSet("Vologda");
        application.getDaoFactory().getCityDao().save(c);
        System.out.println(c);

        StreetDataSet st = new StreetDataSet("Mira", c);
        application.getDaoFactory().getStreetDao().save(st);

        BuildingDataSet b = new BuildingDataSet("1A", st);
        application.getDaoFactory().getBuildingDao().save(b);

        b = new BuildingDataSet("2", st);
        application.getDaoFactory().getBuildingDao().save(b);

        b = new BuildingDataSet("3", st);
        application.getDaoFactory().getBuildingDao().save(b);

        c.setName("Sokol");
        application.getDaoFactory().getCityDao().save(c);


        CustomerDataSet cus = new CustomerDataSet("user", "", b, "113", "c0000111");
        application.getDaoFactory().getCustomerDao().save(cus);

        for (Iterator iterator = application.getDaoFactory().getBuildingDao().getAll().iterator(); iterator.hasNext();){
            BuildingDataSet building = (BuildingDataSet) iterator.next();
            System.out.println( building );
        }

        CityDataSetDao CityDao = application.getDaoFactory().getCityDao();

        System.out.println( CityDao.save(new CityDataSet("Vologda")) );
        System.out.println( CityDao.save(new CityDataSet("Moscow")) );
        System.out.println( CityDao.save(new CityDataSet("Spb")) );
        CityDao.delete( CityDao.getById(4) );
        CityDao.getById(10);
        System.out.println( CityDao.getAll() );
        System.out.println( CityDao.getByName("Vologda") );
        System.out.println( CityDao.getByName("Spb") );

        StreetDataSetDao StreetDao = application.getDaoFactory().getStreetDao();
        StreetDao.save(new StreetDataSet("Koneva", c));
        StreetDao.save(new StreetDataSet("Kirova", c));
        System.out.println( StreetDao.getAll() );
        System.out.println( StreetDao.getByCity(c) );

        BuildingDataSetDao buildingDao = application.getDaoFactory().getBuildingDao();
        System.out.println( buildingDao.getAll() );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByCity(c) );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByStreet(st) );

        System.out.println( buildingDao.getById(1) );


        DaoFactory daoF = application.getDaoFactory();

        CustomerDataSetDao customerDao = daoF.getCustomerDao();
        CustomerDataSet customer = new CustomerDataSet("alex", "", b, "123", "+7(921)12345678");
        customerDao.save(customer);

        System.out.println( customerDao.getAll() );
        System.out.println( customerDao.getByCity(c) );
        System.out.println( customerDao.getByStreet(st) );
        System.out.println( customerDao.getByBuilding(b) );

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

        application.setDaoFactory(daoF);
        UserAccountService userAccountService = new UserAccountServiceImpl(application);
        application.setUserAccountService(userAccountService);
        application.addByType(UserAccountService.class, userAccountService);

        UserAccountService accSer = new UserAccountServiceImpl(application);
        System.out.println(accSer.addUser("lex", "123456", "Lex", "---", AccessLevel.ADMIN));

        application.getLogService().info("Starting http server");

        HttpServer server = new HttpServerImpl(application);
        server.start();
        server.join();

        System.out.println( "Hello World!" );
        System.exit(0);
    }
}
