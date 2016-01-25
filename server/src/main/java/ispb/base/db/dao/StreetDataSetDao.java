package ispb.base.db.dao;

import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.filter.DataSetFilter;

import java.util.List;

public interface StreetDataSetDao {

    long save(StreetDataSet street);
    void delete(StreetDataSet street);
    List<StreetDataSet> getList(DataSetFilter filter);
    List<StreetDataSet> getAll();
    List<StreetDataSet> getByCity(CityDataSet city);
    StreetDataSet getById(long id);
    StreetDataSet getByName(CityDataSet city, String name);
}
