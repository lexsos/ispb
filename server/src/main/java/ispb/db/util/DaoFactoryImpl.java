package ispb.db.util;


import ispb.base.db.dao.*;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.QueryBuilder;
import ispb.base.resources.AppResources;
import ispb.db.dao.*;
import org.hibernate.SessionFactory;

public class DaoFactoryImpl implements DaoFactory {

    private final SessionFactory sessions;
    private final AppResources resources;
    private final QueryBuilder queryBuilder;

    public DaoFactoryImpl(SessionFactory sessions, AppResources resources, QueryBuilder queryBuilder){
        this.sessions = sessions;
        this.resources = resources;
        this.queryBuilder = queryBuilder;
    }

    public CityDataSetDao getCityDao(){
        return new CityDataSetDaoImpl(sessions, resources);
    }

    public StreetDataSetDao getStreetDao(){
        return new StreetDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public BuildingDataSetDao getBuildingDao(){
        return new BuildingDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public CustomerDataSetDao getCustomerDao(){
        return new CustomerDataSetDaoImpl(sessions);
    }

    public UserDataSetDao getUserDao(){ return new UserDataSetDaoImpl(sessions, resources); }

    public CustomerSummeryViewDao getCustomerSummeryViewDao(){
        return new CustomerSummeryViewDaoImpl(sessions, resources, queryBuilder);
    }

    public PaymentGroupDataSetDao getPaymentGroupDao(){
        return new PaymentGroupDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public PaymentDataSetDao getPaymentDao(){
        return new PaymentDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public TariffDataSetDao getTariffDao(){
        return new TariffDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public TariffAssignmentDataSetDao getTariffAssignmentDao(){
        return new TariffAssignmentDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public CustomerStatusDataSetDao getCustomerStatusDao(){
        return new CustomerStatusDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public AutoPaymentJournalDataSetDao getAutoPaymentJournalDataSetDao(){
        return new AutoPaymentJournalDataSetDaoImpl(sessions, resources);
    }

    public TariffRadiusAttributeDataSetDao getTariffRadiusAttributeDataSetDao(){
        return new TariffRadiusAttributeDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public RadiusUserDataSetDao getRadiusUserDataSetDao(){
        return new RadiusUserDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public RadiusUserAttributeDataSetDao getRadiusUserAttributeDataSetDao(){
        return new RadiusUserAttributeDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public RadiusClientDataSetDao getRadiusClientDataSetDao(){
        return new RadiusClientDataSetDaoImpl(sessions, resources, queryBuilder);
    }

    public RadiusClientParameterDataSetDao getRadiusClientParameterDataSetDao(){
        return new RadiusClientParameterDataSetDaoImpl(sessions, resources, queryBuilder);
    }
}
