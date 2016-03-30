package ispb.db.dao;


import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.field.FieldSetDescriptor;
import ispb.base.db.filter.*;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;


import java.util.List;

public class BuildingDataSetDaoImpl extends BaseDao implements BuildingDataSetDao {

    private final QueryBuilder queryBuilder;
    private final FieldSetDescriptor fieldsDescriptor;
    private final String hqlListTmpl;

    public BuildingDataSetDaoImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        super(sessions);
        this.queryBuilder = queryBuilder;

        fieldsDescriptor = loadFieldDescriptor(resources, "BuildingDataSetDaoImpl/fieldSetDescriptor.json");
        hqlListTmpl = resources.getAsString(this.getClass(), "BuildingDataSetDaoImpl/hql_list.tmpl");
    }

    public long save(BuildingDataSet building){
        return this.saveEntity(building) ;
    }

    public void delete(BuildingDataSet building){
        this.markAsDeleted(building);
    }

    public List<BuildingDataSet> getList(DataSetFilter filter, DataSetSort sort){
        return getListWithoutDeleted(BuildingDataSet.class,
                filter, sort, null,
                queryBuilder, fieldsDescriptor, hqlListTmpl);
    }

    public BuildingDataSet getById(long id){
        return this.getEntityById(BuildingDataSet.class, id);
    }
}
