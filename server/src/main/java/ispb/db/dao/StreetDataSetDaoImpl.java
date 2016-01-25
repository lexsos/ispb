package ispb.db.dao;

import java.util.Iterator;
import java.util.List;

import ispb.base.db.filter.*;
import ispb.base.resources.AppResources;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.db.util.BaseDao;


public class StreetDataSetDaoImpl extends BaseDao implements StreetDataSetDao {

    private AppResources resources;
    private WhereBuilder whereBuilder;

    public StreetDataSetDaoImpl(SessionFactory sessions, AppResources resources, WhereBuilder whereBuilder){
        super(sessions);
        this.resources = resources;
        this.whereBuilder = whereBuilder;
    }

    public long save(StreetDataSet street){
        return this.saveEntity(street);
    }

    public void delete(StreetDataSet street){
        this.markAsDeleted(street);
    }

    public List<StreetDataSet> getList(DataSetFilter filter){

        filter.add(new DataSetFilterItem("deleteAt", CmpOperator.IS_NULL, null));
        FieldSetDescriptor fields = resources.getJsonAsObject(
                this.getClass(),
                "StreetDataSetDaoImpl/fieldSetDescriptor.json",
                FieldSetDescriptor.class
        );
        WhereStatement whereStatement = whereBuilder.buildAnd(fields, filter);
        String tmpl = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/hql_list.tmpl");
        String hql = tmpl.replaceAll("\\{where_statement\\}", whereStatement.getWhere());

        Object result = this.doTransaction(
                (session, transaction) -> {
                    Query query = session.createQuery(hql);
                    for (Iterator<String> i = whereStatement.getParameters().keySet().iterator(); i.hasNext();){
                        String parameterName = i.next();
                        Object value = whereStatement.getParameters().get(parameterName);
                        query.setParameter(parameterName, value);
                    }
                    return query.list();
                }
        );
        return (List<StreetDataSet>)result;
    }

    public List<StreetDataSet> getAll(){
        String hql = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/getAll.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).list()
        );
        return (List<StreetDataSet>)result;
    }

    public List<StreetDataSet> getByCity(CityDataSet city){
        String hql = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/getByCity.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("city", city).list()
        );
        return (List<StreetDataSet>)result;
    }

    public StreetDataSet getById(long id){
        return (StreetDataSet)this.getEntityById(StreetDataSet.class, id);
    }

    public StreetDataSet getByName(CityDataSet city, String name){
        String hql = resources.getAsString(this.getClass(), "StreetDataSetDaoImpl/getByCityByStreeName.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("city", city).setParameter("name", name).list()
        );
        List<StreetDataSet> list = (List<StreetDataSet>)result;
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
}
