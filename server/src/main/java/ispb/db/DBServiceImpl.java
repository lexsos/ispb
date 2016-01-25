package ispb.db;

import ispb.base.Application;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.db.util.DaoFactoryImpl;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import ispb.base.service.DBService;


public class DBServiceImpl implements DBService {

    private DaoFactory daoFactory = null;
    private Config config = null;
    private AppResources appResources = null;
    private LogService logService = null;
    private WhereBuilder whereBuilder;
    private QueryBuilder queryBuilder;


    public DBServiceImpl(Config config, AppResources appResources, LogService logService, WhereBuilder whereBuilder, QueryBuilder queryBuilder){
        this.config = config;
        this.appResources = appResources;
        this.logService = logService;
        this.whereBuilder = whereBuilder;
        this.queryBuilder = queryBuilder;
    }

    public void clearDB(){
        Flyway flyway = new Flyway();
        flyway.setDataSource(getConnectionString(), config.getAsStr("db.user"), config.getAsStr("db.password"));
        flyway.clean();
    }

    public void migrate(){
        Flyway flyway = new Flyway();
        flyway.setDataSource(getConnectionString(), config.getAsStr("db.user"), config.getAsStr("db.password"));
        try {
            flyway.migrate();
        }
        catch (FlywayException e){
            logService.error("Can't perform DB migration");
            throw e;
        }

    }

    public DaoFactory getDaoFactory(){
        if (daoFactory == null)
            daoFactory = new DaoFactoryImpl(getSessionFactory(), appResources, whereBuilder, queryBuilder);
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
