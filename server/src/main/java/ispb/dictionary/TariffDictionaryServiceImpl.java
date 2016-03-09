package ispb.dictionary;

import ispb.base.db.container.TariffContainer;
import ispb.base.db.dao.TariffDataSetDao;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;


public class TariffDictionaryServiceImpl implements TariffDictionaryService {

    private DaoFactory daoFactory;

    public TariffDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort){
        TariffDataSetDao dao = daoFactory.getTariffDao();
        return dao.getList(filter, sort, null);
    }

    public TariffDataSet create(TariffContainer container) throws AlreadyExistException{

        if (exist(container.getName()))
            throw new AlreadyExistException();

        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = new TariffDataSet();
        tariff.update(container);
        dao.save(tariff);
        return tariff;
    }

    public TariffDataSet update(TariffContainer container) throws AlreadyExistException, NotFoundException{
        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = dao.getById(container.getId());

        if (tariff == null)
            throw new NotFoundException();

        TariffDataSet otherTariff = getByName(container.getName());
        if (otherTariff != null && otherTariff.getId() != tariff.getId())
            throw new AlreadyExistException();

        tariff.update(container);
        dao.save(tariff);
        return tariff;
    }

    public void delete(long tariffId) throws NotFoundException{
        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = dao.getById(tariffId);
        if (tariff == null)
            throw new NotFoundException();
        dao.delete(tariff);
        return;
    }

    public boolean exist(String tariffName){

        return getByName(tariffName) != null;
    }

    public TariffDataSet getByName(String tariffName){
        DataSetFilter filter = new DataSetFilter();
        filter.add("name", CmpOperator.EQ, tariffName);
        List<TariffDataSet> tariffList = getList(filter, null);
        if (tariffList.isEmpty())
            return null;
        return tariffList.get(0);
    }
}
