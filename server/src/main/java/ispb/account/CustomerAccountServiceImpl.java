package ispb.account;

import ispb.base.Application;
import ispb.base.db.container.CustomerContainer;
import ispb.base.db.dao.BuildingDataSetDao;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.CustomerStatusDataSetDao;
import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.eventsys.EventMessage;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.CheckCustomerStatusMsg;
import ispb.base.eventsys.message.CustomerStatusAppliedMsg;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CustomerAccountServiceImpl implements CustomerAccountService {

    private DaoFactory daoFactory;
    private Application application;

    public CustomerAccountServiceImpl(DaoFactory daoFactory, Application application){
        this.daoFactory = daoFactory;
        this.application = application;
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

    public CustomerStatusDataSet setStatus(long customerId, CustomerStatus status, CustomerStatusCause cause, Date from)
            throws NotFoundException{
        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();
        CustomerStatusDataSetDao statusDao = daoFactory.getCustomerStatusDao();

        CustomerDataSet customer = customerDao.getById(customerId);
        if (customer == null)
            throw new NotFoundException();

        CustomerStatusDataSet statusDataSet = new CustomerStatusDataSet();
        statusDataSet.setCustomer(customer);
        statusDataSet.setStatus(status);
        statusDataSet.setCause(cause);
        statusDataSet.setApplyAt(from);
        statusDao.save(statusDataSet);

        // send message about new status
        sendMsg(new CheckCustomerStatusMsg());

        return statusDataSet;
    }

    public CustomerStatusDataSet managerSetStatus(long customerId, CustomerStatus status) throws NotFoundException{
        return setStatus(customerId, status, CustomerStatusCause.MANAGER, new Date());
    }

    public List<CustomerStatusDataSet> getStatusList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        CustomerStatusDataSetDao dao = daoFactory.getCustomerStatusDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getStatusCount(DataSetFilter filter){
        CustomerStatusDataSetDao dao = daoFactory.getCustomerStatusDao();
        return dao.getCount(filter);
    }

    private List<CustomerStatusDataSet> getStatusListForApply(Date until){
        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.LT_EQ, until);
        filter.add("processed", CmpOperator.EQ, false);
        return getStatusList(filter, null, null);
    }

    private void sendMsg(EventMessage message){
        EventSystem eventSystem = application.getByType(EventSystem.class);
        if (eventSystem != null)
            eventSystem.pushMessage(message);
    }

    public void applyNewStatuses(){
        // TODO: make batch processing
        CustomerStatusDataSetDao dao = daoFactory.getCustomerStatusDao();
        List<CustomerStatusDataSet> newStatuses = getStatusListForApply(new Date());
        CustomerStatusAppliedMsg msg = new CustomerStatusAppliedMsg();

        for (Iterator<CustomerStatusDataSet> i = newStatuses.iterator(); i.hasNext(); ){
            CustomerStatusDataSet status = i.next();
            status.setProcessed(true);
            dao.save(status);
            msg.addCustomerId(status.getCustomer());
        }

        // send message about new status applied
        sendMsg(msg);
    }

}
