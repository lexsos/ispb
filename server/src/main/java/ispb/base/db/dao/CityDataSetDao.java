package ispb.base.db.dao;

import ispb.base.db.dataset.CityDataSet;
import java.util.List;

public interface CityDataSetDao {

    long save(CityDataSet city);
    void delete(CityDataSet city);
    List<CityDataSet> getAll();
    CityDataSet getByName(String name);
    CityDataSet getById(long id);
}
