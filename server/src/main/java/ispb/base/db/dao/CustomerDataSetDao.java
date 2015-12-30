package ispb.base.db.dao;

import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.StreetDataSet;

import java.util.List;

public interface CustomerDataSetDao {
    long save(CustomerDataSet customer);
    void delete(CustomerDataSet customer);
    CustomerDataSet getById(long id);
    List<CustomerDataSet> getAll();
    List<CustomerDataSet> getByCity(CityDataSet city);
    List<CustomerDataSet> getByStreet(StreetDataSet street);
    List<CustomerDataSet> getByBuilding(BuildingDataSet building);
}
