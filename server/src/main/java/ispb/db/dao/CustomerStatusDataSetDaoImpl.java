package ispb.db.dao;

import ispb.base.db.dao.CustomerStatusDataSetDao;
import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class CustomerStatusDataSetDaoImpl extends BaseDao implements CustomerStatusDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;

    public CustomerStatusDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "CustomerStatusDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "CustomerStatusDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "CustomerStatusDataSetDaoImpl/tmpl_count.hql");
    }

    public long save(CustomerStatusDataSet status){
        return saveEntity(status);
    }

    public void delete(CustomerStatusDataSet status){
        markAsDeleted(status);
    }

    public List<CustomerStatusDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(CustomerStatusDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public CustomerStatusDataSet getById(long id){
        return getEntityById(CustomerStatusDataSet.class, id);
    }
}
