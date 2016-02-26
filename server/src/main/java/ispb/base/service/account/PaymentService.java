package ispb.base.service.account;


import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import java.util.List;

public interface PaymentService {
    List<PaymentDataSet> getPayments(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getPaymentCount(DataSetFilter filter);
}
