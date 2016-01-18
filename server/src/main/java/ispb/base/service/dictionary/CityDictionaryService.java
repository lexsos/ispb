package ispb.base.service.dictionary;

import ispb.base.db.dataset.CityDataSet;

import java.util.List;

public interface CityDictionaryService {
    CityDataSet getById(long id);
    List<CityDataSet> getAll();
    CityDataSet add(String name);
    void update(long id, String name);
    void delete(long id);
    boolean exist(String name);
}
