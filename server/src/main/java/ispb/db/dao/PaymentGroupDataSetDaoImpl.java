package ispb.db.dao;


import ispb.base.db.dao.PaymentGroupDataSetDao;
import ispb.base.db.dataset.PaymentGroupDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class PaymentGroupDataSetDaoImpl extends BaseDao implements PaymentGroupDataSetDao {

    private QueryBuilder queryBuilder;
    private FieldSetDescriptor fieldsDescriptor;
    private String hqlListTmpl;
    private String hqlCountTmpl;

    public PaymentGroupDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "PaymentGroupDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "PaymentGroupDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "PaymentGroupDataSetDaoImpl/tmpl_count.hql");
    }

    public long save(PaymentGroupDataSet paymentGroup){
        return saveEntity(paymentGroup);
    }

    public void delete(PaymentGroupDataSet paymentGroup){
        markAsDeleted(paymentGroup);
    }

    public List<PaymentGroupDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        List result = getQueryAsList(hqlListTmpl, queryBuilder, fieldsDescriptor, newFilter, sort, pagination);

        return (List<PaymentGroupDataSet>)result;
    }

    public long getCount(DataSetFilter filter){
        DataSetFilter newFilter;
        if (filter == null)
            newFilter = new DataSetFilter();
        else
            newFilter = filter.getCopy();
        newFilter.add("deleteAt", CmpOperator.IS_NULL, null);

        return getRowCount(hqlCountTmpl, queryBuilder, fieldsDescriptor, newFilter);
    }
}
