package ispb.base.service;

import ispb.base.db.utils.DaoFactory;
import org.hibernate.SessionFactory;

public interface DBService {

    DaoFactory getDaoFactory();
    void clearDB();
    void migrate();

    SessionFactory getSessionFactory();
}
