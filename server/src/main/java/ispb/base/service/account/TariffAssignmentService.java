package ispb.base.service.account;

import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.BadDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface TariffAssignmentService {

    List<TariffAssignmentDataSet> getHistory(long customerId);
    List<TariffAssignmentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);
    void assignTariff(long customerId, long tariffId, Date fromDate) throws NotFoundException, BadDateException;

    void applyNewAssignment();
}
