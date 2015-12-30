package ispb.base;

import org.hibernate.SessionFactory;

public interface DBService {
    SessionFactory getSessionFactory();
}
