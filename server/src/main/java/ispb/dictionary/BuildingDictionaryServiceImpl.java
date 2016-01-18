package ispb.dictionary;

import ispb.base.db.dataset.BuildingDataSet;
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

    public BuildingDataSet create(long streetId, String buildingName)
            throws AlreadyExistException, DicElementNotFoundException{
        return null;
    }

    public BuildingDataSet update(long buildingId, long streetId, String buildingName)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException{
        return null;
    }

    public void delete(long buildingId) throws NotFoundException{
        return;
    }

    public boolean exist(long streetId, String buildingName){
        return false;
    }
}
