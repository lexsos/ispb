package ispb.base.db.dao;


import ispb.base.db.dataset.PaymentGroupDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface PaymentGroupDataSetDao {
    long save(PaymentGroupDataSet paymentGroup);
    void delete(PaymentGroupDataSet paymentGroup);
    List<PaymentGroupDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    PaymentGroupDataSet getById(long id);
}
