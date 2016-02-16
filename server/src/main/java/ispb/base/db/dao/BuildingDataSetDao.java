package ispb.base.db.dao;


import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;

import java.util.List;

public interface BuildingDataSetDao {
    long save(BuildingDataSet building);
    void delete(BuildingDataSet building);
    List<BuildingDataSet> getAll();
    List<BuildingDataSet> getList(DataSetFilter filter, DataSetSort sort);
    List<BuildingDataSet> getList(DataSetFilter filter);
    List<BuildingDataSet> getByCity(CityDataSet city);
    List<BuildingDataSet> getByStreet(StreetDataSet street);
    BuildingDataSet getById(long id);
    BuildingDataSet getByName(StreetDataSet street, String buildingName);
}
