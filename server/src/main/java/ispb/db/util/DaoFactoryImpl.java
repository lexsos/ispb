package ispb.db.util;


import ispb.base.db.dao.*;
import ispb.base.db.filter.WhereBuilder;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.dao.*;
import org.hibernate.SessionFactory;

public class DaoFactoryImpl implements DaoFactory {

    private SessionFactory sessions;
    private AppResources resourceses;
    private QueryBuilder queryBuilder;

    public DaoFactoryImpl(SessionFactory sessions, AppResources resourceses, QueryBuilder queryBuilder){
        this.sessions = sessions;
        this.resourceses = resourceses;
        this.queryBuilder = queryBuilder;
    }

    public CityDataSetDao getCityDao(){
        return new CityDataSetDaoImpl(sessions, resourceses);
    }

    public StreetDataSetDao getStreetDao(){
        return new StreetDataSetDaoImpl(sessions, resourceses, queryBuilder);
    }

    public BuildingDataSetDao getBuildingDao(){
        return new BuildingDataSetDaoImpl(sessions, resourceses, queryBuilder);
    }

    public CustomerDataSetDao getCustomerDao(){
        return new CustomerDataSetDaoImpl(sessions);
    }

    public UserDataSetDao getUserDao(){ return new UserDataSetDaoImpl(sessions, resourceses); }

    public CustomerSummeryViewDao getCustomerSummeryViewDao(){
        return new CustomerSummeryViewDaoImpl(sessions, resourceses, queryBuilder);
    }

    public PaymentGroupDataSetDao getPaymentGroupDao(){
        return new PaymentGroupDataSetDaoImpl(sessions, resourceses, queryBuilder);
    }

    public PaymentDataSetDao getPaymentDao(){
        return new PaymentDataSetDaoImpl(sessions, resourceses, queryBuilder);
    }

    public TariffDataSetDao getTariffDao(){
        return new TariffDataSetDaoImpl(sessions, resourceses, queryBuilder);
    }
}
