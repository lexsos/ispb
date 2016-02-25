package ispb.base.db.dao;


import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface PaymentDataSetDao {
    long save(PaymentDataSet payment);
    void delete(PaymentDataSet payment);
    List<PaymentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
}
