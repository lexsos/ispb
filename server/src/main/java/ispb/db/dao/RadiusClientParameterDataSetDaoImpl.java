package ispb.db.dao;

import ispb.base.db.dao.RadiusClientParameterDataSetDao;
import ispb.base.db.dataset.RadiusClientParameterDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;


public class RadiusClientParameterDataSetDaoImpl extends BaseDao implements RadiusClientParameterDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;

    public RadiusClientParameterDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        fieldsDescriptor = loadFieldDescriptor(resources, "RadiusClientParameterDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "RadiusClientParameterDataSetDaoImpl/tmpl_list.hql");
    }

    public long save(RadiusClientParameterDataSet parameter){
        return saveEntity(parameter);
    }

    public void delete(RadiusClientParameterDataSet parameter){
        markAsDeleted(parameter);
    }

    public List<RadiusClientParameterDataSet> getList(DataSetFilter filter, DataSetSort sort){
        return getListWithoutDeleted(RadiusClientParameterDataSet.class,
                filter, sort, null,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }
    public RadiusClientParameterDataSet getById(long id){
        return getEntityById(RadiusClientParameterDataSet.class, id);
    }
}
