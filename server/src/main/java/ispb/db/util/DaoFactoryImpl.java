package ispb.db.util;


import ispb.base.db.dao.*;
import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.AppResources;
import ispb.db.dao.*;
import org.hibernate.SessionFactory;

public class DaoFactoryImpl implements DaoFactory {

    private SessionFactory sessions;
    private AppResources resourceses;

    public DaoFactoryImpl(SessionFactory sessions, AppResources resourceses){
        this.sessions = sessions;
        this.resourceses = resourceses;
    }

    public CityDataSetDao getCityDao(){
        return new CityDataSetDaoImpl(sessions, resourceses);
    }

    public StreetDataSetDao getStreetDao(){
        return new StreetDataSetDaoImpl(sessions, resourceses);
    }

    public BuildingDataSetDao getBuildingDao(){
        return new BuildingDataSetDaoImpl(sessions, resourceses);
    }

    public CustomerDataSetDao getCustomerDao(){
        return new CustomerDataSetDaoImpl(sessions, resourceses);
    }

    public UserDataSetDao getUserDao(){ return new UserDataSetDaoImpl(sessions, resourceses); }
}
