package ispb.base.service.dictionary;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface CityDictionaryService {
    CityDataSet getById(long id);
    List<CityDataSet> getAll();
    CityDataSet create(String name) throws AlreadyExistException;
    CityDataSet update(long id, String name) throws AlreadyExistException, NotFoundException;
    void delete(long id) throws NotFoundException;
    boolean exist(String name);
}
