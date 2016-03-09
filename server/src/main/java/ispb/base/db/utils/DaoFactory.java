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
}
