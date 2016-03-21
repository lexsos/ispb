package ispb.db.dao;


import ispb.base.db.dao.PaymentDataSetDao;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

public class PaymentDataSetDaoImpl extends BaseDao implements PaymentDataSetDao {

    private QueryBuilder queryBuilder;
    private FieldSetDescriptor fieldsDescriptor;
    private String hqlListTmpl;
    private String hqlCountTmpl;
    private String hqlBalance;

    public PaymentDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "PaymentDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "PaymentDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "PaymentDataSetDaoImpl/tmpl_count.hql");
        hqlBalance = resources.getAsString(this.getClass(), "PaymentDataSetDaoImpl/balance.hql");
    }

    public long save(PaymentDataSet payment){
        return saveEntity(payment);
    }

    public void delete(PaymentDataSet payment){
        markAsDeleted(payment);
    }

    public List<PaymentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        DataSetFilter newFilter;
        if (filter == null)
           newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getQueryAsList(hqlListTmpl, queryBuilder, fieldsDescriptor, newFilter, sort, pagination);
    }

    public long getCount(DataSetFilter filter){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new  DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getRowCount(hqlCountTmpl, queryBuilder, fieldsDescriptor, newFilter);
    }

    public PaymentDataSet getById(long id){
        return getEntityById(PaymentDataSet.class, id);
    }

    public double getBalance(CustomerDataSet customer, Date dateFor){
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hqlBalance).
                                setParameter("applyAt", dateFor).
                                setParameter("customerId", customer.getId()).
                                uniqueResult()
        );
        if (result instanceof Double)
            return (Double)result;
        return 0;
    }
}
