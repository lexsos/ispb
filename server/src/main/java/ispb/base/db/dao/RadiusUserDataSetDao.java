package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface RadiusUserDataSetDao {
    long save(RadiusUserDataSet radiusUser);
    void delete(RadiusUserDataSet radiusUser);
    List<RadiusUserDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    RadiusUserDataSet getById(long id);
}
