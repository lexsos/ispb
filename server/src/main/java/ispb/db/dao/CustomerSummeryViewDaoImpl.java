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

    private AppResources resources;
    private WhereBuilder whereBuilder;
    private QueryBuilder queryBuilder;

    public CustomerSummeryViewDaoImpl(SessionFactory sessions, AppResources resources, WhereBuilder whereBuilder, QueryBuilder queryBuilder){
        super(sessions);
        this.resources = resources;
        this.whereBuilder = whereBuilder;
        this.queryBuilder = queryBuilder;
    }

    public List<CustomerSummeryView> getList(DataSetFilter filter){
        FieldSetDescriptor fields = resources.getJsonAsObject(
                this.getClass(),
                "CustomerSummeryViewDaoImpl/fieldSetDescriptor.json",
                FieldSetDescriptor.class
        );
        WhereStatement whereStatement = whereBuilder.buildAnd(fields, filter);
        String tmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_list.hql");

        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(tmpl, whereStatement, session).list()
        );
        return (List<CustomerSummeryView>)result;
    }

    public long getCount(DataSetFilter filter){
        FieldSetDescriptor fields = resources.getJsonAsObject(
                this.getClass(),
                "CustomerSummeryViewDaoImpl/fieldSetDescriptor.json",
                FieldSetDescriptor.class
        );
        WhereStatement whereStatement = whereBuilder.buildAnd(fields, filter);
        String tmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_count.hql");

        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(tmpl, whereStatement, session).uniqueResult()
        );
        return (Long)result;
    }
}
