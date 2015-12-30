package ispb.base.db.dao;

import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.dataset.CityDataSet;
import java.util.List;

public interface StreetDataSetDao {

    long save(StreetDataSet street);
    void delete(StreetDataSet street);
    List<StreetDataSet> getAll();
    List<StreetDataSet> getByCity(CityDataSet city);
    StreetDataSet getById(long id);
}
