package ispb.dictionary;

import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public class CityDictionaryServiceImpl implements CityDictionaryService {

    private DaoFactory daoFactory;

    public CityDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public CityDataSet getById(long id){
        return daoFactory.getCityDao().getById(id);
    }

    public List<CityDataSet> getAll(){
        return daoFactory.getCityDao().getAll();
    }

    public CityDataSet create(String name) throws AlreadyExistException {
        CityDataSetDao dao = daoFactory.getCityDao();
        if (dao.getByName(name) != null)
            throw new AlreadyExistException();

        CityDataSet city = new CityDataSet();
        city.setName(name);
        dao.save(city);
        return city;
    }

    public CityDataSet update(long id, String name) throws AlreadyExistException, NotFoundException {
        CityDataSetDao dao = daoFactory.getCityDao();
        CityDataSet city = dao.getById(id);

        if (city == null)
            throw new NotFoundException();

        CityDataSet otherCity = dao.getByName(name);
        if (otherCity != null && otherCity.getId() != city.getId())
            throw new AlreadyExistException();

        city.setName(name);
        dao.save(city);
        return city;
    }

    public void delete(long id) throws NotFoundException {
        CityDataSetDao dao = daoFactory.getCityDao();
        CityDataSet city = dao.getById(id);
        if (city == null)
            throw new NotFoundException();
        dao.delete(city);
    }

    public boolean exist(String name) {
        CityDataSetDao dao = daoFactory.getCityDao();
        if (dao.getByName(name) != null)
            return true;
        return false;
    }
}
