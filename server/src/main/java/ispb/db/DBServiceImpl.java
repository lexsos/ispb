package ispb.db;

import ispb.base.Application;
import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.Config;
import ispb.db.util.DaoFactoryImpl;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import ispb.base.service.DBService;


public class DBServiceImpl implements DBService {

    private Application application;
    private DaoFactory daoFactory = null;

    public DBServiceImpl(Application application){
        this.application = application;
    }

    public void clearDB(){
        Config config = application.getConfig();
        Flyway flyway = new Flyway();
        flyway.setDataSource(getConnectionString(), config.getAsStr("db.user"), config.getAsStr("db.password"));
        flyway.clean();
    }

    public void migrate(){

        Config config = application.getConfig();
        Flyway flyway = new Flyway();
        flyway.setDataSource(getConnectionString(), config.getAsStr("db.user"), config.getAsStr("db.password"));
        flyway.migrate();
    }

    public DaoFactory getDaoFactory(){
        if (daoFactory == null)
            daoFactory = new DaoFactoryImpl(getSessionFactory(), application.getAppResources());
        return daoFactory;
    }

    private SessionFactory getSessionFactory(){

        Configuration configuration = getHbmConf();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private String getConnectionString(){
        Config config = application.getConfig();
        StringBuilder connStr = new StringBuilder();
        connStr.append("jdbc:postgresql://");
        connStr.append(config.getAsStr("db.host"));
        connStr.append(":");
        connStr.append(config.getAsStr("db.port"));
        connStr.append("/");
        connStr.append(config.getAsStr("db.base"));
        return connStr.toString();
    }

    private Configuration getHbmConf(){
        Config config = application.getConfig();
        Configuration  hdmConfiguration = new Configuration().configure( "hibernate.cfg.xml");

        hdmConfiguration.setProperty("hibernate.connection.url", getConnectionString());
        hdmConfiguration.setProperty("hibernate.connection.username", config.getAsStr("db.user"));
        hdmConfiguration.setProperty("hibernate.connection.password", config.getAsStr("db.password"));
        hdmConfiguration.setProperty("hibernate.show_sql", config.getAsStr("db.show_sql"));

        hdmConfiguration.setProperty("hibernate.c3p0.min_size", config.getAsStr("db.pool_min"));
        hdmConfiguration.setProperty("hibernate.c3p0.max_size", config.getAsStr("db.pool_max"));

        return hdmConfiguration;
    }
}
