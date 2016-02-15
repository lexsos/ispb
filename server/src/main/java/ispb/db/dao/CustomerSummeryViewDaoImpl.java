package ispb.db.dao;


import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.filter.*;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class CustomerSummeryViewDaoImpl extends BaseDao implements CustomerSummeryViewDao {

    private WhereBuilder whereBuilder;
    private QueryBuilder queryBuilder;
    private String hqlListTmpl;
    private String hqlCountTmpl;
    private FieldSetDescriptor fieldsDescriptor;

    public CustomerSummeryViewDaoImpl(SessionFactory sessions, AppResources resources, WhereBuilder whereBuilder, QueryBuilder queryBuilder){
        super(sessions);
        this.whereBuilder = whereBuilder;
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_count.hql");
        fieldsDescriptor = loadFieldDescriptor(resources, "CustomerSummeryViewDaoImpl/fieldSetDescriptor.json");
    }

    public List<CustomerSummeryView> getList(DataSetFilter filter){
        WhereStatement whereStatement = whereBuilder.buildAnd(fieldsDescriptor, filter);
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlListTmpl, whereStatement, session).list()
        );
        return (List<CustomerSummeryView>)result;
    }

    public long getCount(DataSetFilter filter){
        WhereStatement whereStatement = whereBuilder.buildAnd(fieldsDescriptor, filter);
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlCountTmpl, whereStatement, session).uniqueResult()
        );
        return (Long)result;
    }
}
