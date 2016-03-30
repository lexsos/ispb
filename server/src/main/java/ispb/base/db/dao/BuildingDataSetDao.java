package ispb.base.db.dao;


import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;

import java.util.List;


public interface BuildingDataSetDao {
    long save(BuildingDataSet building);
    void delete(BuildingDataSet building);
    List<BuildingDataSet> getList(DataSetFilter filter, DataSetSort sort);
    BuildingDataSet getById(long id);
}
