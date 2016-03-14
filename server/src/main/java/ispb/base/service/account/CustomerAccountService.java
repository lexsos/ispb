package ispb.base.service.account;


import ispb.base.db.container.CustomerContainer;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface CustomerAccountService {
    List<CustomerSummeryView> getSummeryList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);

    CustomerSummeryView getSummeryByContractNumber(String contractNumber);
    CustomerSummeryView getSummeryById(long customerId);

    CustomerSummeryView updateSummery(CustomerContainer container)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException;
    CustomerSummeryView createSummery(CustomerContainer container)
            throws AlreadyExistException, DicElementNotFoundException;

    boolean contractNumberExist(String contractNumber);

    void setStatus(long customerId, CustomerStatus status, CustomerStatusCause cause, Date from)
            throws NotFoundException;
}
