package ispb.db.dao;


import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.resources.AppResources;
import ispb.db.util.BaseDao;
import org.hibernate.SessionFactory;

import java.util.List;

public class CustomerDataSetDaoImpl extends BaseDao implements CustomerDataSetDao {

    private AppResources resources;

    public CustomerDataSetDaoImpl(SessionFactory sessions, AppResources resources){
        super(sessions);
        this.resources = resources;
    }

    public long save(CustomerDataSet customer){
        return this.saveEntity(customer);
    }

    public void delete(CustomerDataSet customer){
        this.markAsDeleted(customer);
    }

    public CustomerDataSet getById(long id){
        return (CustomerDataSet)this.getEntityById(CustomerDataSet.class, id);
    }

    public List<CustomerDataSet> getAll(){
        String hql = resources.getAsString(this.getClass(), "CustomerDataSetDaoImpl/getAll.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).list()
        );
        return (List<CustomerDataSet>)result;
    }

    public List<CustomerDataSet> getByCity(CityDataSet city){
        String hql = resources.getAsString(this.getClass(), "CustomerDataSetDaoImpl/getByCity.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("city", city).list()
        );
        return (List<CustomerDataSet>)result;
    }

    public List<CustomerDataSet> getByStreet(StreetDataSet street){
        String hql = resources.getAsString(this.getClass(), "CustomerDataSetDaoImpl/getByStreet.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("street", street).list()
        );
        return (List<CustomerDataSet>)result;
    }

    public List<CustomerDataSet> getByBuilding(BuildingDataSet building){
        String hql = resources.getAsString(this.getClass(), "CustomerDataSetDaoImpl/getByBuilding.hql");
        Object result = this.doTransaction(
                (session, transaction) ->
                        session.createQuery(hql).setParameter("building", building).list()
        );
        return (List<CustomerDataSet>)result;
    }
}
