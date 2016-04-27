package ispb.base.db.dao;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface RadiusClientDataSetDao {

    long save(RadiusClientDataSet client);
    void delete(RadiusClientDataSet client);
    RadiusClientDataSet getById(long id);

    long getCount(DataSetFilter filter);
    List<RadiusClientDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
}
