package ispb.account;

import ispb.base.db.container.CustomerContainer;
import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public class CustomerAccountServiceImpl implements CustomerAccountService {

    private DaoFactory daoFactory;

    public CustomerAccountServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<CustomerSummeryView> getSummeryList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getCount(DataSetFilter filter){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        return dao.getCount(filter);
    }

    public CustomerSummeryView getSummeryByContractNumber(String contractNumber){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        DataSetFilter filter = new DataSetFilter();
        filter.add("contractNumber", CmpOperator.EQ, contractNumber);
        List<CustomerSummeryView> result = dao.getList(filter, null, null);
        if (result != null && !result.isEmpty())
            return result.get(0);
        return null;
    }

    public CustomerSummeryView getSummeryById(long customerId){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        return dao.getById(customerId);
    }

    public CustomerSummeryView updateSummery(CustomerContainer container)
            throws AlreadyExistException, DicElementNotFoundException, NotFoundException{

        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();
        BuildingDataSetDao buildingDao = daoFactory.getBuildingDao();

        CustomerDataSet customer = customerDao.getById(container.getId());
        if (customer == null)
            throw new NotFoundException();

        BuildingDataSet building = buildingDao.getById(container.getBuildingId());
        if (building == null)
            throw new DicElementNotFoundException();

        CustomerSummeryView otherCustomer = getSummeryByContractNumber(container.getContractNumber());
        if (otherCustomer != null && otherCustomer.getCustomer().getId() != customer.getId())
            throw new AlreadyExistException();

        customer.setName(container.getName());
        customer.setSurname(container.getSurname());
        customer.setPatronymic(container.getPatronymic());
        customer.setPassport(container.getPassport());
        customer.setPhone(container.getPhone());
        customer.setComment(container.getComment());
        customer.setContractNumber(container.getContractNumber());
        customer.setBuilding(building);
        customer.setRoom(container.getRoom());

        customerDao.save(customer);

        return getSummeryById(customer.getId());
    }

    public CustomerSummeryView createSummery(CustomerContainer container)
            throws AlreadyExistException, DicElementNotFoundException{

        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();
        BuildingDataSetDao buildingDao = daoFactory.getBuildingDao();

        BuildingDataSet building = buildingDao.getById(container.getBuildingId());
        if (building == null)
            throw new DicElementNotFoundException();

        if ( contractNumberExist(container.getContractNumber()) )
            throw new AlreadyExistException();

        CustomerDataSet customer = new CustomerDataSet();
        customer.setName(container.getName());
        customer.setSurname(container.getSurname());
        customer.setPatronymic(container.getPatronymic());
        customer.setPassport(container.getPassport());
        customer.setPhone(container.getPhone());
        customer.setComment(container.getComment());
        customer.setContractNumber(container.getContractNumber());
        customer.setBuilding(building);
        customer.setRoom(container.getRoom());
        long customerId = customerDao.save(customer);

        return getSummeryById(customerId);
    }

    public boolean contractNumberExist(String contractNumber){
        return getSummeryByContractNumber(contractNumber) != null;
    }
}
