package ispb.db.dao;

import java.util.List;

import ispb.base.resources.AppResources;
import org.hibernate.SessionFactory;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.db.util.BaseDao;


public class CityDataSetDaoImpl extends BaseDao implements CityDataSetDao {

    private final AppResources resources;

    public CityDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);
        this.resources = resources;
    }

    public long save(CityDataSet city){
        return this.saveEntity(city);
    }

    public void delete(CityDataSet city){
        this.markAsDeleted(city);
    }

    public List<CityDataSet> getAll(){
        String hql = resources.getAsString(this.getClass(), "CityDataSetDaoImpl/getAll.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).list()
        );
        return (List<CityDataSet>)result;
    }

    public CityDataSet getByName(String name){
        String hql = resources.getAsString(this.getClass(), "CityDataSetDaoImpl/getByName.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("name", name).list()
        );
        List<CityDataSet> list = (List<CityDataSet>)result;
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public CityDataSet getById(long id){
        return this.getEntityById(CityDataSet.class, id);
    }

}
