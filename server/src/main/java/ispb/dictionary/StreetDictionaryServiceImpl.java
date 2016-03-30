package ispb.dictionary;


import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.dictionary.StreetDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public class StreetDictionaryServiceImpl implements StreetDictionaryService {

    private final DaoFactory daoFactory;

    public StreetDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public StreetDataSet create(long cityId, String streetName) throws AlreadyExistException, DicElementNotFoundException {
        StreetDataSetDao streetDao = daoFactory.getStreetDao();
        CityDataSetDao cityDao = daoFactory.getCityDao();

        CityDataSet city = cityDao.getById(cityId);
        if (city == null)
            throw new DicElementNotFoundException();

        StreetDataSet otherStreet = getByName(city, streetName);
        if (otherStreet != null)
            throw new AlreadyExistException();

        StreetDataSet street = new StreetDataSet();
        street.setCity(city);
        street.setName(streetName);
        streetDao.save(street);
        return street;
    }

    public StreetDataSet update(long streetId, long cityId, String streetName)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException{
        StreetDataSetDao streetDao = daoFactory.getStreetDao();
        CityDataSetDao cityDao = daoFactory.getCityDao();

        StreetDataSet street = streetDao.getById(streetId);
        if (street == null)
            throw new NotFoundException();

        CityDataSet city = cityDao.getById(cityId);
        if (city == null)
            throw new DicElementNotFoundException();

        StreetDataSet otherStreet = getByName(city, streetName);
        if (otherStreet != null && otherStreet.getId() != street.getId())
            throw new AlreadyExistException();

        street.setCity(city);
        street.setName(streetName);
        streetDao.save(street);
        return street;
    }

    public void delete(long id) throws NotFoundException{
        StreetDataSetDao dao = daoFactory.getStreetDao();
        StreetDataSet street = dao.getById(id);
        if (street == null)
            throw new NotFoundException();
        dao.delete(street);
    }

    public boolean exist(long  cityId, String name) {
        CityDataSetDao cityDao = daoFactory.getCityDao();

        CityDataSet city = cityDao.getById(cityId);
        return city != null && getByName(city, name) != null;

    }

    public List<StreetDataSet> getList(DataSetFilter filter){
        StreetDataSetDao streetDao = daoFactory.getStreetDao();
        return streetDao.getList(filter);
    }

    private StreetDataSet getByName(CityDataSet city, String name){
        DataSetFilter filter = new DataSetFilter();
        filter.add("cityId", CmpOperator.EQ, city.getId());
        filter.add("name", CmpOperator.EQ, name);

        List<StreetDataSet> list = getList(filter);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
}
