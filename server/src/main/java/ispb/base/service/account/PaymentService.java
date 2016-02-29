package ispb.base.service.account;


import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.IncorrectDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface PaymentService {
    List<PaymentDataSet> getPayments(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getPaymentCount(DataSetFilter filter);

    void addVirtualPayment(long customerId, double sum, Date until, String comment)
            throws NotFoundException, IncorrectDateException;
    void addPayment(long customerId, double sum, String comment)
            throws NotFoundException;
}
