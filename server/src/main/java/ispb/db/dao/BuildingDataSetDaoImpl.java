package ispb.db.dao;


import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;


import java.util.List;

public class BuildingDataSetDaoImpl extends BaseDao implements BuildingDataSetDao {

    private AppResources resources;

    public BuildingDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);
        this.resources = resources;
    }

    public long save(BuildingDataSet building){
        return this.saveEntity(building) ;
    }

    public void delete(BuildingDataSet building){
        this.markAsDeleted(building);
    }

    public List<BuildingDataSet> getAll(){
        String hql = resources.getAsString(this.getClass(), "BuildingDataSetDaoImpl/getAll.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).list()
        );
        return (List<BuildingDataSet>)result;
    }

    public List<BuildingDataSet> getByCity(CityDataSet city){
        String hql = resources.getAsString(this.getClass(), "BuildingDataSetDaoImpl/getByCity.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("city", city).list()
        );
        return (List<BuildingDataSet>)result;
    }

    public List<BuildingDataSet> getByStreet(StreetDataSet street){
        String hql = resources.getAsString(this.getClass(), "BuildingDataSetDaoImpl/getByStreet.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("street", street).list()
        );
        return (List<BuildingDataSet>)result;
    }

    public BuildingDataSet getByName(StreetDataSet street, String buildingName){
        String hql = resources.getAsString(this.getClass(), "BuildingDataSetDaoImpl/getByStreetIdByName.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("street", street).setParameter("name", buildingName).list()
        );
        List<BuildingDataSet> list = (List<BuildingDataSet>)result;
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public BuildingDataSet getById(long id){
        return (BuildingDataSet)this.getEntityById(BuildingDataSet.class, id);
    }
}
