package ispb.dictionary;

import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.dictionary.BuildingDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;


public class BuildingDictionaryServiceImpl implements BuildingDictionaryService {

    private DaoFactory daoFactory;

    public BuildingDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<BuildingDataSet> getAll(){
        return daoFactory.getBuildingDao().getAll();
    }

    public List<BuildingDataSet> getList(DataSetFilter filter, DataSetSort sort){
        return daoFactory.getBuildingDao().getList(filter, sort);
    }

    public BuildingDataSet create(long streetId, String buildingName)
            throws AlreadyExistException, DicElementNotFoundException{
        BuildingDataSetDao buildingDao = daoFactory.getBuildingDao();
        StreetDataSetDao streetDao = daoFactory.getStreetDao();

        StreetDataSet street = streetDao.getById(streetId);
        if (street == null)
            throw new DicElementNotFoundException();

        BuildingDataSet otherBuilding = buildingDao.getByName(street, buildingName);
        if (otherBuilding != null)
            throw new AlreadyExistException();

        BuildingDataSet building = new BuildingDataSet();
        building.setName(buildingName);
        building.setStreet(street);
        buildingDao.save(building);

        return building;
    }

    public BuildingDataSet update(long buildingId, long streetId, String buildingName)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException{
        BuildingDataSetDao buildingDao = daoFactory.getBuildingDao();
        StreetDataSetDao streetDao = daoFactory.getStreetDao();

        BuildingDataSet building = buildingDao.getById(buildingId);
        if (building == null)
            throw new NotFoundException();

        StreetDataSet street = streetDao.getById(streetId);
        if (street == null)
            throw new DicElementNotFoundException();

        BuildingDataSet otherBuilding = buildingDao.getByName(street, buildingName);
        if (otherBuilding != null && otherBuilding.getId() != building.getId())
            throw new AlreadyExistException();

        building.setName(buildingName);
        building.setStreet(street);
        buildingDao.save(building);
        return building;
    }

    public void delete(long buildingId) throws NotFoundException{
        BuildingDataSetDao buildingDao = daoFactory.getBuildingDao();
        BuildingDataSet building = buildingDao.getById(buildingId);
        if (building == null)
            throw new NotFoundException();
        buildingDao.delete(building);
    }

    public boolean exist(long streetId, String buildingName){
        StreetDataSet street = daoFactory.getStreetDao().getById(streetId);
        if (street == null)
            return false;

        if (daoFactory.getBuildingDao().getByName(street, buildingName) == null )
            return false;

        return true;
    }
}
