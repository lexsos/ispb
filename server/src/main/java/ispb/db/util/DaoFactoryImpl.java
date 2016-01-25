package ispb.db.util;


import ispb.base.db.dao.*;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.resources.AppResources;
import ispb.db.dao.*;
import org.hibernate.SessionFactory;

public class DaoFactoryImpl implements DaoFactory {

    private SessionFactory sessions;
    private AppResources resourceses;
    private WhereBuilder whereBuilder;

    public DaoFactoryImpl(SessionFactory sessions, AppResources resourceses, WhereBuilder whereBuilder){
        this.sessions = sessions;
        this.resourceses = resourceses;
        this.whereBuilder = whereBuilder;
    }

    public CityDataSetDao getCityDao(){
        return new CityDataSetDaoImpl(sessions, resourceses);
    }

    public StreetDataSetDao getStreetDao(){
        return new StreetDataSetDaoImpl(sessions, resourceses, whereBuilder);
    }

    public BuildingDataSetDao getBuildingDao(){
        return new BuildingDataSetDaoImpl(sessions, resourceses);
    }

    public CustomerDataSetDao getCustomerDao(){
        return new CustomerDataSetDaoImpl(sessions, resourceses);
    }

    public UserDataSetDao getUserDao(){ return new UserDataSetDaoImpl(sessions, resourceses); }

    public CustomerSummeryViewDao getCustomerSummeryViewDao(){
        return new CustomerSummeryViewDaoImpl(sessions);
    }
}
