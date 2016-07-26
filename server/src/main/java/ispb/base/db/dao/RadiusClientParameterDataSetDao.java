package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusClientParameterDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;

import java.util.List;

public interface RadiusClientParameterDataSetDao {
    long save(RadiusClientParameterDataSet parameter);
    void delete(RadiusClientParameterDataSet parameter);
    List<RadiusClientParameterDataSet> getList(DataSetFilter filter, DataSetSort sort);
    RadiusClientParameterDataSet getById(long id);
}
