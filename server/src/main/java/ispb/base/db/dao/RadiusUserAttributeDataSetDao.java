package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusUserAttributeDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface RadiusUserAttributeDataSetDao {

    long save(RadiusUserAttributeDataSet attribute);
    void delete(RadiusUserAttributeDataSet attribute);
    RadiusUserAttributeDataSet getById(long id);

    long getCount(DataSetFilter filter);
    List<RadiusUserAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
}
