package ispb.db.util;

import java.util.Date;
import java.util.List;

import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.*;
import ispb.base.resources.AppResources;
import org.hibernate.Query;
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

    protected FieldSetDescriptor loadFieldDescriptor(AppResources resources, String resourceName){
        return resources.getJsonAsObject(this.getClass(), resourceName, FieldSetDescriptor.class);
    }

    protected List getQueryAsList(String hqlListTmpl,
                                  QueryBuilder queryBuilder,
                                  FieldSetDescriptor fieldsDescriptor,
                                  DataSetFilter filter,
                                  DataSetSort sort,
                                  Pagination pagination){

        Object result = this.doTransaction(
                (session, transaction) -> {

                    Query query = queryBuilder.getQuery(hqlListTmpl, session, fieldsDescriptor, filter, sort);

                    if (pagination != null && pagination.isValid()){
                        query.setFirstResult(pagination.getStart());
                        query.setMaxResults(pagination.getLimit());
                    }

                    return query.list();
                }
        );

        if (result instanceof List)
            return (List)result;
        else
            return null;
    }

    protected long getRowCount(String hqlCountTmpl,
                               QueryBuilder queryBuilder,
                               FieldSetDescriptor fieldsDescriptor,
                               DataSetFilter filter){

        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlCountTmpl, session, fieldsDescriptor, filter, null).uniqueResult()
        );

        if (result instanceof Long)
            return (Long)result;
        return 0;
    }
}
