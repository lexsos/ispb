package ispb.db.dao;


import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.*;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
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

    public List<CustomerSummeryView> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
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

    public CustomerSummeryView getById(long id){
        return this.getEntityById(CustomerSummeryView.class, id);
    }
}
