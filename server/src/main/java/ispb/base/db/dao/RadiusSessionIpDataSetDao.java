package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusSessionIpDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.Date;
import java.util.List;

public interface RadiusSessionIpDataSetDao {
    long save( RadiusSessionIpDataSet address);
    void delete(RadiusSessionIpDataSet address);
    List<RadiusSessionIpDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    RadiusSessionIpDataSet getById(long id);

    void eraseOld(Date olderThen);
}
