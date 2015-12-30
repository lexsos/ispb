package ispb.db.dao;

import java.util.List;

import ispb.base.resources.AppResources;
import org.hibernate.SessionFactory;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.db.util.BaseDao;


public class StreetDataSetDaoImpl extends BaseDao implements StreetDataSetDao {

    private AppResources resources;

    public StreetDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);
        this.resources = resources;
    }

    public long save(StreetDataSet street){
        return this.saveEntity(street);
    }

    public void delete(StreetDataSet street){
        this.markAsDeleted(street);
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
}
