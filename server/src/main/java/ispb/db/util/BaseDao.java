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

    private final SessionFactory sessions;

    public BaseDao(SessionFactory sessions){
        this.sessions = sessions;
    }

    @SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("unchecked")
    protected <T> T getEntityById(Class<T> clazz, long id){
        Object obj = this.doTransaction(
                (session, transaction) -> session.get(clazz, id)
        );

        if (clazz.isInstance(obj))
            return (T)obj;
        return null;
    }

    protected FieldSetDescriptor loadFieldDescriptor(AppResources resources, String resourceName){
        return resources.getJsonAsObject(this.getClass(), resourceName, FieldSetDescriptor.class);
    }

    private List getQueryAsList(String hqlListTmpl,
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

    private long getRowCount(String hqlCountTmpl,
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


    protected <T> List<T> getListWithoutDeleted(@SuppressWarnings("UnusedParameters") Class<T> clazz,
                                                DataSetFilter filter,
                                                DataSetSort sort,
                                                Pagination pagination,
                                                QueryBuilder queryBuilder,
                                                FieldSetDescriptor fieldsDescriptor,
                                                String hqlListTmpl){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getQueryAsList(hqlListTmpl, queryBuilder, fieldsDescriptor, newFilter, sort, pagination);
    }

    protected long getCountWithoutDeleted(DataSetFilter filter,
                                          QueryBuilder queryBuilder,
                                          FieldSetDescriptor fieldsDescriptor,
                                          String hqlCountTmpl){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getRowCount(hqlCountTmpl, queryBuilder, fieldsDescriptor, newFilter);
    }
}
