package ispb.base.db.utils;


import org.hibernate.Session;
import org.hibernate.Transaction;

@FunctionalInterface
public interface DaoTransaction {
    Object run(Session session, Transaction transaction);
}
