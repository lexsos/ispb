package ispb.db.dao;


import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.*;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class CustomerSummeryViewDaoImpl extends BaseDao implements CustomerSummeryViewDao {

    private QueryBuilder queryBuilder;
    private String hqlListTmpl;
    private String hqlCountTmpl;
    private FieldSetDescriptor fieldsDescriptor;

    public CustomerSummeryViewDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_count.hql");
        fieldsDescriptor = loadFieldDescriptor(resources, "CustomerSummeryViewDaoImpl/fieldSetDescriptor.json");
    }

    public List<CustomerSummeryView> getList(DataSetFilter filter){
        // TODO: add sort
        DataSetSort sort = new DataSetSort();
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlListTmpl, session, fieldsDescriptor, filter, sort).list()
        );
        return (List<CustomerSummeryView>)result;
    }

    public long getCount(DataSetFilter filter){
        // TODO: sort
        DataSetSort sort = new DataSetSort();
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlCountTmpl, session, fieldsDescriptor, filter, sort).uniqueResult()
        );
        return (Long)result;
    }
}
