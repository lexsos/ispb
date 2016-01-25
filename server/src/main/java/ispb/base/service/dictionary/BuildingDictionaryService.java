package ispb.base.service.dictionary;


import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface BuildingDictionaryService {
    List<BuildingDataSet> getAll();
    BuildingDataSet create(long streetId, String buildingName) throws AlreadyExistException, DicElementNotFoundException;
    BuildingDataSet update(long buildingId, long streetId, String buildingName)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException;
    void delete(long buildingId) throws NotFoundException;
    boolean exist(long streetId, String buildingName);
}