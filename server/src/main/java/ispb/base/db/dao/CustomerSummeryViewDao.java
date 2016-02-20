package ispb.base.db.dao;

import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;

import java.util.List;

public interface CustomerSummeryViewDao {

    List<CustomerSummeryView> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    CustomerSummeryView getById(long id);
}
