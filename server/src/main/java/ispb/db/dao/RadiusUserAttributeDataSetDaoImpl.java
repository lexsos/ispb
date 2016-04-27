package ispb.db.dao;


import ispb.base.db.dao.RadiusUserAttributeDataSetDao;
import ispb.base.db.dataset.RadiusUserAttributeDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class RadiusUserAttributeDataSetDaoImpl extends BaseDao implements RadiusUserAttributeDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;
    private final String hqlCountTmpl;

    public RadiusUserAttributeDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);

        this.queryBuilder = queryBuilder;
        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusUserAttributeDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusUserAttributeDataSetDaoImpl/tmpl_list.hql");
        hqlCountTmpl = resources.getAsString(this.getClass(), "RadiusUserAttributeDataSetDaoImpl/tmpl_count.hql");
    }

    public long save(RadiusUserAttributeDataSet attribute){
        return saveEntity(attribute);
    }

    public void delete(RadiusUserAttributeDataSet attribute){
        markAsDeleted(attribute);
    }

    public RadiusUserAttributeDataSet getById(long id){
        return getEntityById(RadiusUserAttributeDataSet.class, id);
    }

    public long getCount(DataSetFilter filter){
        return getCountWithoutDeleted(filter, queryBuilder, fieldsDescriptor, hqlCountTmpl);
    }

    public List<RadiusUserAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        return getListWithoutDeleted(RadiusUserAttributeDataSet.class,
                filter, sort, pagination,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }
}
