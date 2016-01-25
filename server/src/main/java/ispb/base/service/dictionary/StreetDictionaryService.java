package ispb.base.service.dictionary;


import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface StreetDictionaryService {
    List<StreetDataSet> getList(DataSetFilter filter);
    List<StreetDataSet> getAll();
    List<StreetDataSet> getByCity(long cityId);
    StreetDataSet create(long cityId, String streetName) throws AlreadyExistException, DicElementNotFoundException;
    StreetDataSet update(long streetId, long cityId, String streetName)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException;
    void delete(long id) throws NotFoundException;
    boolean exist(long  cityId, String streetName);
}
