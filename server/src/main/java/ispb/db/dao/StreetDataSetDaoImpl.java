package ispb.db.dao;

import java.util.List;

import ispb.base.db.filter.*;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import org.hibernate.SessionFactory;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.db.util.BaseDao;


public class StreetDataSetDaoImpl extends BaseDao implements StreetDataSetDao {

    private AppResources resources;
    private WhereBuilder whereBuilder;
    private QueryBuilder queryBuilder;
    private FieldSetDescriptor fieldsDescriptor;
    private String hqlListTmpl;

    public StreetDataSetDaoImpl(SessionFactory sessions, AppResources resources, WhereBuilder whereBuilder, QueryBuilder queryBuilder){
        super(sessions);
        this.resources = resources;
        this.whereBuilder = whereBuilder;
        this.queryBuilder = queryBuilder;

        hqlListTmpl = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/hql_list.tmpl");
        fieldsDescriptor = loadFieldDescriptor(resources, "StreetDataSetDaoImpl/fieldSetDescriptor.json");
    }

    public long save(StreetDataSet street){
        return this.saveEntity(street);
    }

    public void delete(StreetDataSet street){
        this.markAsDeleted(street);
    }

    public List<StreetDataSet> getList(DataSetFilter filter){
        WhereStatement whereStatement = whereBuilder.buildAnd(fieldsDescriptor, filter, deleteAtFilter);
        Object result = this.doTransaction(
                (session, transaction) ->
                        queryBuilder.getQuery(hqlListTmpl, whereStatement, session).list()
        );
        return (List<StreetDataSet>)result;
    }

    public List<StreetDataSet> getAll(){
        DataSetFilter filter = new DataSetFilter();
        return getList(filter);
    }

    public List<StreetDataSet> getByCity(CityDataSet city){
        DataSetFilter filter = new DataSetFilter();
        filter.add("cityId", CmpOperator.EQ, city.getId());
        return getList(filter);
    }

    public StreetDataSet getById(long id){
        return (StreetDataSet)this.getEntityById(StreetDataSet.class, id);
    }

    public StreetDataSet getByName(CityDataSet city, String name){
        DataSetFilter filter = new DataSetFilter();
        filter.add("cityId", CmpOperator.EQ, city.getId());
        filter.add("name", CmpOperator.EQ, name);
        List<StreetDataSet> list = getList(filter);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
}
