package ispb.base.service.dictionary;


import ispb.base.db.container.RadiusClientContainer;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface RadiusClientDictionaryService {
    long getCount(DataSetFilter filter);
    List<RadiusClientDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);

    RadiusClientDataSet create(RadiusClientContainer container)
            throws AlreadyExistException, InvalidIpAddressException;
    RadiusClientDataSet update(long clientId, RadiusClientContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException;

    void delete(long clientId) throws NotFoundException;
    boolean ip4exist(String ip4Address);
}
