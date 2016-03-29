package ispb.db.dao;


import ispb.base.db.dao.CustomerSummeryViewDao;
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

    private final QueryBuilder queryBuilder;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;
    private final FieldSetDescriptor fieldsDescriptor;

    public CustomerSummeryViewDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "CustomerSummeryViewDaoImpl/tmpl_count.hql");
        fieldsDescriptor = loadFieldDescriptor(resources, "CustomerSummeryViewDaoImpl/fieldSetDescriptor.json");
    }

    public List<CustomerSummeryView> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(CustomerSummeryView.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);

    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public CustomerSummeryView getById(long id){
        return this.getEntityById(CustomerSummeryView.class, id);
    }
}
