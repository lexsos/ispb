package ispb.base.db.dao;


import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface CustomerStatusDataSetDao {
    long save(CustomerStatusDataSet status);
    void delete(CustomerStatusDataSet status);
    List<CustomerStatusDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    CustomerStatusDataSet getById(long id);
}
