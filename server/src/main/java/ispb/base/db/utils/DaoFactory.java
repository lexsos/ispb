package ispb.base.db.utils;


import ispb.base.db.dao.*;

public interface DaoFactory {
    CityDataSetDao getCityDao();
    StreetDataSetDao getStreetDao();
    BuildingDataSetDao getBuildingDao();
    CustomerDataSetDao getCustomerDao();
    UserDataSetDao getUserDao();
    CustomerSummeryViewDao getCustomerSummeryViewDao();
    PaymentGroupDataSetDao getPaymentGroupDao();
    PaymentDataSetDao getPaymentDao();
    TariffDataSetDao getTariffDao();
    TariffAssignmentDataSetDao getTariffAssignmentDao();
    CustomerStatusDataSetDao getCustomerStatusDao();
    AutoPaymentJournalDataSetDao getAutoPaymentJournalDataSetDao();
    TariffRadiusAttributeDataSetDao getTariffRadiusAttributeDataSetDao();
    RadiusUserDataSetDao getRadiusUserDataSetDao();
    RadiusUserAttributeDataSetDao getRadiusUserAttributeDataSetDao();
    RadiusClientDataSetDao getRadiusClientDataSetDao();
    RadiusClientParameterDataSetDao getRadiusClientParameterDataSetDao();
    RadiusSessionDataSetDao getRadiusSessionDataSetDao();
    RadiusSessionAttributeDataSetDao getRadiusSessionAttributeDataSetDao();
    RadiusSessionIpDataSetDao getRadiusSessionIpDataSetDao();
}
