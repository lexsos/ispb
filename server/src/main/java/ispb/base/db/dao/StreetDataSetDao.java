package ispb.base.db.dao;

import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.filter.DataSetFilter;

import java.util.List;

public interface StreetDataSetDao {

    long save(StreetDataSet street);
    void delete(StreetDataSet street);
    List<StreetDataSet> getList(DataSetFilter filter);
    StreetDataSet getById(long id);
}
