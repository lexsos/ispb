package ispb.dictionary;

import ispb.base.db.container.TariffContainer;
import ispb.base.db.container.TariffRadiusAttributeContainer;
import ispb.base.db.dao.TariffDataSetDao;
import ispb.base.db.dao.TariffRadiusAttributeDataSetDao;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;


public class TariffDictionaryServiceImpl implements TariffDictionaryService {

    private final DaoFactory daoFactory;

    public TariffDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<TariffDataSet> getList(DataSetFilter filter, DataSetSort sort){
        TariffDataSetDao dao = daoFactory.getTariffDao();
        return dao.getList(filter, sort, null);
    }

    public List<TariffRadiusAttributeDataSet> getAttributeList(DataSetFilter filter,
                                                               DataSetSort sort,
                                                               Pagination pagination){
        TariffRadiusAttributeDataSetDao dao = daoFactory.getTariffRadiusAttributeDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public List<TariffRadiusAttributeDataSet> getAttributeList(long tariffId, RadiusAttributeCondition condition){
        DataSetFilter filter = new DataSetFilter();
        filter.add("tariffId", CmpOperator.EQ, tariffId);
        filter.add("condition", CmpOperator.EQ, condition);
        return getAttributeList(filter, null, null);
    }

    public TariffDataSet create(TariffContainer container) throws AlreadyExistException{

        if (exist(container.getName()))
            throw new AlreadyExistException();

        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = new TariffDataSet();
        update(tariff, container);
        dao.save(tariff);
        return tariff;
    }

    public TariffDataSet update(long tariffId, TariffContainer container) throws AlreadyExistException, NotFoundException{
        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = dao.getById(tariffId);

        if (tariff == null)
            throw new NotFoundException();

        TariffDataSet otherTariff = getByName(container.getName());
        if (otherTariff != null && otherTariff.getId() != tariff.getId())
            throw new AlreadyExistException();

        update(tariff, container);
        dao.save(tariff);
        return tariff;
    }

    public TariffRadiusAttributeDataSet createAttribute(TariffRadiusAttributeContainer container)
            throws NotFoundException{
        TariffDataSetDao tariffDao = daoFactory.getTariffDao();
        TariffRadiusAttributeDataSetDao attributeDao = daoFactory.getTariffRadiusAttributeDataSetDao();

        TariffDataSet tariff = tariffDao.getById(container.getTariffId());
        if (tariff == null)
            throw new NotFoundException();

        TariffRadiusAttributeDataSet attribute = new TariffRadiusAttributeDataSet();
        attribute.setTariff(tariff);
        attribute.setAttributeName(container.getAttributeName());
        attribute.setAttributeValue(container.getAttributeValue());
        attribute.setCondition(container.getCondition());
        attributeDao.save(attribute);
        return attribute;
    }

    public TariffRadiusAttributeDataSet updateAttribute(long attributeId, TariffRadiusAttributeContainer container)
            throws NotFoundException{
        TariffDataSetDao tariffDao = daoFactory.getTariffDao();
        TariffRadiusAttributeDataSetDao attributeDao = daoFactory.getTariffRadiusAttributeDataSetDao();

        TariffRadiusAttributeDataSet attribute = attributeDao.getById(attributeId);
        if (attribute == null)
            throw new NotFoundException();

        TariffDataSet tariff = tariffDao.getById(container.getTariffId());
        if (tariff == null)
            throw new NotFoundException();

        attribute.setTariff(tariff);
        attribute.setAttributeName(container.getAttributeName());
        attribute.setAttributeValue(container.getAttributeValue());
        attribute.setCondition(container.getCondition());
        attributeDao.save(attribute);
        return attribute;
    }

    public void delete(long tariffId) throws NotFoundException{
        TariffDataSetDao dao = daoFactory.getTariffDao();
        TariffDataSet tariff = dao.getById(tariffId);
        if (tariff == null)
            throw new NotFoundException();
        dao.delete(tariff);
    }

    public void deleteAttribute(long attributeId) throws NotFoundException{
        TariffRadiusAttributeDataSetDao dao = daoFactory.getTariffRadiusAttributeDataSetDao();
        TariffRadiusAttributeDataSet attribute = dao.getById(attributeId);
        if (attribute == null)
            throw new NotFoundException();
        dao.delete(attribute);
    }

    public boolean exist(String tariffName){

        return getByName(tariffName) != null;
    }

    private TariffDataSet getByName(String tariffName){
        DataSetFilter filter = new DataSetFilter();
        filter.add("name", CmpOperator.EQ, tariffName);
        List<TariffDataSet> tariffList = getList(filter, null);
        if (tariffList.isEmpty())
            return null;
        return tariffList.get(0);
    }

    private void update(TariffDataSet tariff, TariffContainer container){
        tariff.setDeleteAt(container.getDeleteAt());

        tariff.setName(container.getName());

        tariff.setAutoDailyPayment(container.isAutoDailyPayment());
        tariff.setDailyPayment(container.getDailyPayment());
        tariff.setOffThreshold(container.getOffThreshold());
    }
}
