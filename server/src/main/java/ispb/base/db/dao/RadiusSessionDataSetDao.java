package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface RadiusSessionDataSetDao {
    long save(RadiusSessionDataSet session);
    void delete(RadiusSessionDataSet session);
    List<RadiusSessionDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    RadiusSessionDataSet getById(long id);
}
