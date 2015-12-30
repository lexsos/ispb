package ispb.db.util;

import java.util.Date;

import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DaoTransaction;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public class BaseDao {

    private SessionFactory sessions;

    public BaseDao(SessionFactory sessions){
        this.sessions = sessions;
    }

    protected SessionFactory getSessions(){
        return this.sessions;
    }

    protected Object doTransaction(DaoTransaction daoTransaction){
        Session session = this.getSessions().openSession();
        Transaction transaction = null;
        Object result = null;

        try {
            transaction = session.beginTransaction();
            result = daoTransaction.run(session, transaction);
            transaction.commit();
        }
        catch (RuntimeException e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
        finally {
            session.close();
        }

        return result;
    }

    protected long saveEntity(Identifiable entity){

        if(entity instanceof CreatedTimestampable){
            CreatedTimestampable obj = (CreatedTimestampable)entity;
            if (obj.getCreateAt() == null)
                obj.setCreateAt(new Date());
        }

        return (long)this.doTransaction(
                (session, transaction) -> {
                    session.saveOrUpdate(entity);
                    return entity.getId();
                }
        );
    }

    protected void markAsDeleted(DeletedMarkable entity){
        entity.setDeleteAt(new Date());
        this.doTransaction(
                (session, transaction) -> {
                    session.saveOrUpdate(entity);
                    return null;
                }
        );
    }

    protected Object getEntityById(Class type, long id){
        return this.doTransaction(
                (session, transaction) -> session.get(type, id)
        );
    }
}
