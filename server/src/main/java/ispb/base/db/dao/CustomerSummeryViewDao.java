package ispb.base.db.dao;

import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.view.CustomerSummeryView;

import java.util.List;

public interface CustomerSummeryViewDao {

    List<CustomerSummeryView> getList(DataSetFilter filter);
    long getCount(DataSetFilter filter);
}
