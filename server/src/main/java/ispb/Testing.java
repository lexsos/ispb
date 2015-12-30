package ispb;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

import ispb.base.Application;
import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dataset.*;
import ispb.base.db.utils.DaoFactory;
import ispb.base.frontend.HttpServer;
import ispb.base.resources.Config;
import ispb.base.service.UserAccountService;
import ispb.db.dao.BuildingDataSetDaoImpl;
import ispb.db.dao.CityDataSetDaoImpl;
import ispb.db.dao.StreetDataSetDaoImpl;
import ispb.db.util.DaoFactoryImpl;
import ispb.frontend.HttpServerImpl;
import ispb.resources.AppResourcesImpl;
import ispb.resources.ConfigImpl;
import ispb.users.UserAccountServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

import ispb.base.service.DBService;
import ispb.db.DBServiceImpl;

import org.flywaydb.core.Flyway;


public class Testing
{
    public static void main( String[] args ) throws Exception
    {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost:5432/ispb", "ispb", "123456");
        flyway.clean();

        flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost:5432/ispb", "ispb", "123456");
        flyway.migrate();

        DBService db = new DBServiceImpl();
        SessionFactory sf = db.getSessionFactory();
        Session s = sf.openSession();
        s.beginTransaction();

        CityDataSet c = new CityDataSet("Vologda");
        s.save(c);

        System.out.println(c);

        StreetDataSet st = new StreetDataSet("Mira", c);
        s.save(st);

        BuildingDataSet b = new BuildingDataSet("1A", st);
        s.save(b);

        b = new BuildingDataSet("2", st);
        s.save(b);

        b = new BuildingDataSet("3", st);
        s.save(b);

        c.setName("Sokol");

        CustomerDataSet cus = new CustomerDataSet("user", "", b, "113", "c0000111");
        cus.setCreateAt(new Date());
        s.save(cus);

        s.getTransaction().commit();
        s.close();


        s = sf.openSession();
        s.beginTransaction();
        Query q = s.createQuery("FROM BuildingDataSet WHERE street.city = :city");
        q.setParameter("city", c);
        List blist = q.list();

        for (Iterator iterator = blist.iterator(); iterator.hasNext();){
            BuildingDataSet building = (BuildingDataSet) iterator.next();
            System.out.println( building );
        }
        s.getTransaction().commit();

        s.close();

        CityDataSetDaoImpl CityDao = new CityDataSetDaoImpl(sf, AppResourcesImpl.getInstance());
        System.out.println( CityDao.save(new CityDataSet("Vologda")) );
        System.out.println( CityDao.save(new CityDataSet("Moscow")) );
        System.out.println( CityDao.save(new CityDataSet("Spb")) );
        CityDao.delete( CityDao.getById(4) );
        CityDao.getById(10);
        System.out.println( CityDao.getAll() );
        System.out.println( CityDao.getByName("Vologda") );
        System.out.println( CityDao.getByName("Spb") );

        StreetDataSetDaoImpl StreetDao = new StreetDataSetDaoImpl(sf, AppResourcesImpl.getInstance());
        StreetDao.save(new StreetDataSet("Koneva", c));
        StreetDao.save(new StreetDataSet("Kirova", c));
        System.out.println( StreetDao.getAll() );
        System.out.println( StreetDao.getByCity(c) );

        BuildingDataSetDao buildingDao = new BuildingDataSetDaoImpl(sf, AppResourcesImpl.getInstance());
        System.out.println( buildingDao.getAll() );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByCity(c) );
        System.out.println("--------------------------------------");
        System.out.println( buildingDao.getByStreet(st) );

        System.out.println( buildingDao.getById(1) );


        s = sf.openSession();
        s.beginTransaction();
        q = s.createQuery("select new list(min(building.id), max(building.id)) FROM BuildingDataSet as building");
        blist = q.list();
        s.getTransaction().commit();
        s.close();
        System.out.println(blist);

        DaoFactory daoF = new DaoFactoryImpl(sf, AppResourcesImpl.getInstance());

        CustomerDataSetDao customerDao = daoF.getCustomerDao();
        CustomerDataSet customer = new CustomerDataSet("alex", "", b, "123", "+7(921)12345678");
        customerDao.save(customer);

        System.out.println( customerDao.getAll() );
        System.out.println( customerDao.getByCity(c) );
        System.out.println( customerDao.getByStreet(st) );
        System.out.println( customerDao.getByBuilding(b) );


        Config conf = new ConfigImpl( args[0] );
        System.out.println(conf.getAsStr("db.host"));
        System.out.println(conf.getAsStr("db.port"));
        System.out.println(conf.getAsStr("db.base"));
        System.out.println(conf.getAsStr("db.user"));
        System.out.println(conf.getAsStr("db.passwd"));

        UserDataSet user = new UserDataSet();
        user.setLogin("alex");
        user.setPassword(DigestUtils.sha1Hex("abc123456"));
        user.setSalt("abc");
        user.setAccessLevel(10);
        user.setName("alexander");
        user.setSurname("------");
        System.out.println(daoF.getUserDao().save(user));

        System.out.println(user);
        System.out.println(daoF.getUserDao().getAll());
        System.out.println(daoF.getUserDao().getByLogin("alex"));
        System.out.println(daoF.getUserDao().getByLogin("lex"));

        Application application = ApplicationImpl.getApplication();
        application.setConfig(conf);
        application.setAppResources(AppResourcesImpl.getInstance());
        application.setDaoFactory(daoF);
        application.setUserAccountService(new UserAccountServiceImpl(application));

        UserAccountService accSer = new UserAccountServiceImpl(application);
        System.out.println(accSer.addUser("lex", "123456", "Lex", "---", 20));

        HttpServer server = new HttpServerImpl(application);
        server.start();
        server.join();

        System.out.println( "Hello World!" );
        System.exit(0);
    }
}
