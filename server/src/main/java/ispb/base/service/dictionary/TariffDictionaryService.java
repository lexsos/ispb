package ispb.base.service.dictionary;


import ispb.base.db.container.TariffContainer;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface TariffDictionaryService {
    List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort);

    TariffDataSet create(TariffContainer container) throws AlreadyExistException;
    TariffDataSet update(TariffContainer container) throws AlreadyExistException, NotFoundException;

    void delete(long tariffId) throws NotFoundException;
    boolean exist(String tariffName);
}
