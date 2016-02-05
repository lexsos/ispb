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

    public CustomerDataSetDaoImpl(SessionFactory sessions){
        super(sessions);
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
}
