package ispb.account;


import ispb.base.Application;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.CustomerStatusDataSetDao;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.SortDirection;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.eventsys.EventMessage;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.CheckCustomerStatusMsg;
import ispb.base.eventsys.message.CustomerStatusAppliedMsg;
import ispb.base.service.account.CustomerStatusService;
import ispb.base.service.account.PaymentService;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CustomerStatusServiceImpl implements CustomerStatusService {

    private DaoFactory daoFactory;
    private Application application;

    public CustomerStatusServiceImpl(DaoFactory daoFactory, Application application){
        this.daoFactory = daoFactory;
        this.application = application;
    }

    public CustomerStatusDataSet setStatus(long customerId, CustomerStatus status, CustomerStatusCause cause, Date from)
            throws NotFoundException {
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

    public void checkStatus(CustomerDataSet customer, Date dateFor){
        CustomerStatusDataSet statusDataSet = getStatus(customer, dateFor);
        if (statusDataSet == null)
            return;
        CustomerStatus status = statusDataSet.getStatus();
        CustomerStatusCause cause = statusDataSet.getCause();

        TariffDataSet tariff = getTariffAssignmentService().getTariff(customer, dateFor);
        if (tariff == null)
            return;

        double balance = getPaymentService().getBalance(customer, dateFor);

        try {
            if (status == CustomerStatus.ACTIVE && balance < tariff.getOffThreshold())
                setStatus(customer.getId(), CustomerStatus.INACTIVE, CustomerStatusCause.SYSTEM_FINANCE, dateFor);
            else if(status == CustomerStatus.INACTIVE && cause == CustomerStatusCause.SYSTEM_FINANCE && balance >= tariff.getOffThreshold())
                setStatus(customer.getId(), CustomerStatus.ACTIVE, CustomerStatusCause.SYSTEM_FINANCE, dateFor);
        }
        catch (NotFoundException e){
            // TODO: write error to log
        }
    }

    private CustomerStatusDataSet getStatus(CustomerDataSet customer, Date dateFor){
        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.LT_EQ, dateFor);
        filter.add("processed", CmpOperator.EQ, true);
        filter.add("customerId", CmpOperator.EQ, customer.getId());

        DataSetSort sort = new DataSetSort();
        sort.add("applyAt", SortDirection.DESC);

        Pagination pagination = new Pagination();
        pagination.setLimit(1);
        pagination.setStart(0);

        List<CustomerStatusDataSet> statusList = getStatusList(filter, sort, pagination);
        if (statusList.isEmpty())
            return null;
        return statusList.get(0);
    }

    private TariffAssignmentService getTariffAssignmentService(){
        return application.getByType(TariffAssignmentService.class);
    }

    private PaymentService getPaymentService(){
        return application.getByType(PaymentService.class);
    }
}
