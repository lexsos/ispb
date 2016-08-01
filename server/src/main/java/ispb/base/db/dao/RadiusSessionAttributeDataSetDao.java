package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusSessionAttributeDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface RadiusSessionAttributeDataSetDao {
    long save( RadiusSessionAttributeDataSet attribute);
    void delete(RadiusSessionAttributeDataSet attribute);
    List<RadiusSessionAttributeDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    RadiusSessionAttributeDataSet getById(long id);
}
