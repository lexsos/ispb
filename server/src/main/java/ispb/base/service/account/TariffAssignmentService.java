package ispb.base.service.account;

import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.service.exception.BadDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface TariffAssignmentService {

    List<TariffAssignmentDataSet> getHistory(long customerId);
    void assignTariff(long customerId, long tariffId, Date fromDate) throws NotFoundException, BadDateException;
}
