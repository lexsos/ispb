package ispb.db.dao;


import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.filter.*;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;


import java.util.List;

public class BuildingDataSetDaoImpl extends BaseDao implements BuildingDataSetDao {

    private WhereBuilder whereBuilder;
    private QueryBuilder queryBuilder;
    private FieldSetDescriptor fieldsDescriptor;
    private String hqlListTmpl;

    public BuildingDataSetDaoImpl(SessionFactory sessions, AppResources resources, WhereBuilder whereBuilder, QueryBuilder queryBuilder){
        super(sessions);
        this.whereBuilder = whereBuilder;
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

    public List<BuildingDataSet> getList(DataSetFilter filter){
        WhereStatement whereStatement = whereBuilder.buildAnd(fieldsDescriptor, filter, deleteAtFilter);
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlListTmpl, whereStatement, session).list()
        );
        return (List<BuildingDataSet>)result;
    }

    public List<BuildingDataSet> getAll(){
        DataSetFilter filter = new DataSetFilter();
        return getList(filter);
    }

    public List<BuildingDataSet> getByCity(CityDataSet city){
        DataSetFilter filter = new DataSetFilter();
        filter.add("cityId", CmpOperator.EQ, city.getId());
        return getList(filter);
    }

    public List<BuildingDataSet> getByStreet(StreetDataSet street){
        DataSetFilter filter = new DataSetFilter();
        filter.add("streetId", CmpOperator.EQ, street.getId());
        return getList(filter);
    }

    public BuildingDataSet getByName(StreetDataSet street, String buildingName){
        DataSetFilter filter = new DataSetFilter();
        filter.add("streetId", CmpOperator.EQ, street.getId());
        filter.add("name", CmpOperator.EQ, buildingName);
        List<BuildingDataSet> list = getList(filter);

        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public BuildingDataSet getById(long id){
        return (BuildingDataSet)this.getEntityById(BuildingDataSet.class, id);
    }
}
