package ispb.base.service;

import org.hibernate.SessionFactory;

public interface DBService {
    SessionFactory getSessionFactory();
}
