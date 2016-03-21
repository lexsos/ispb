package ispb.base.service.account;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface CustomerStatusService {

    CustomerStatusDataSet setStatus(CustomerDataSet customer, CustomerStatus status, CustomerStatusCause cause, Date from);
    CustomerStatusDataSet setStatus(long customerId, CustomerStatus status, CustomerStatusCause cause, Date from)
            throws NotFoundException;
    CustomerStatusDataSet managerSetStatus(long customerId, CustomerStatus status) throws NotFoundException;

    List<CustomerStatusDataSet> getStatusList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getStatusCount(DataSetFilter filter);
    CustomerStatusDataSet getStatus(CustomerDataSet customer, Date dateFor);

    void applyNewStatuses();
}
