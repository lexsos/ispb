package ispb.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import ispb.base.service.DBService;


public class DBServiceImpl implements DBService {
    
    public SessionFactory getSessionFactory(){


        Configuration  configuration = new Configuration().configure( "hibernate.cfg.xml");

        configuration.setProperty("hibernate.connection.username", "ispb");
        configuration.setProperty("hibernate.connection.password", "123456");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);



    }
}
