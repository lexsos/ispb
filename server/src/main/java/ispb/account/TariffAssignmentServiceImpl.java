package ispb.account;

import ispb.base.Application;
import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.TariffAssignmentDataSetDao;
import ispb.base.db.dao.TariffDataSetDao;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.SortDirection;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.eventsys.EventMessage;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.CheckTariffAssignmentMsg;
import ispb.base.eventsys.message.TariffAppliedMsg;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.exception.BadDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TariffAssignmentServiceImpl implements TariffAssignmentService {

    private DaoFactory daoFactory;
    private Application application;

    public TariffAssignmentServiceImpl(DaoFactory daoFactory, Application application){
        this.daoFactory = daoFactory;
        this.application = application;
    }

    public List<TariffAssignmentDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        TariffAssignmentDataSetDao dao = daoFactory.getTariffAssignmentDao();
        return dao.getList(filter, sort, pagination);
    }

    public List<TariffAssignmentDataSet> getHistory(long customerId){
        DataSetFilter filter = new DataSetFilter();
        filter.add("customerId", CmpOperator.EQ, customerId);
        return getList(filter, null, null);
    }

    public long getCount(DataSetFilter filter){
        TariffAssignmentDataSetDao dao = daoFactory.getTariffAssignmentDao();
        return dao.getCount(filter);
    }

    public void assignTariff(long customerId, long tariffId, Date fromDate) throws NotFoundException, BadDateException {

        TariffAssignmentDataSetDao assignmentDao = daoFactory.getTariffAssignmentDao();
        TariffDataSetDao tariffDao = daoFactory.getTariffDao();
        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();

        CustomerDataSet customer = customerDao.getById(customerId);
        if (customer == null)
            throw new NotFoundException();

        TariffDataSet tariff = tariffDao.getById(tariffId);
        if (tariff == null)
            throw new NotFoundException();

        if (fromDate == null)
            throw new BadDateException();

        // Delete another assignment at this day
        deleteOtherAssignment(fromDate, customer.getId());

        TariffAssignmentDataSet assignment = new TariffAssignmentDataSet();
        assignment.setApplyAt(startOfDay(fromDate));
        assignment.setCustomer(customer);
        assignment.setTariff(tariff);
        assignmentDao.save(assignment);

        // send message about new tariff assignment
        sendMsg(new CheckTariffAssignmentMsg());
    }

    public void applyNewAssignment(){
        //TODO: make batch processing
        List<TariffAssignmentDataSet> assignments = getListForApply(new Date());
        TariffAssignmentDataSetDao dao = daoFactory.getTariffAssignmentDao();
        TariffAppliedMsg msg = new TariffAppliedMsg();

        for (TariffAssignmentDataSet assignment : assignments) {
            assignment.setProcessed(true);
            dao.save(assignment);
            msg.addCustomer(assignment.getCustomer());
        }

        // send message about applied tariff assignment
        sendMsg(msg);
    }

    public TariffDataSet getTariff(CustomerDataSet customer, Date dateFor){
        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.LT_EQ, dateFor);
        filter.add("processed", CmpOperator.EQ, true);
        filter.add("customerId", CmpOperator.EQ, customer.getId());

        DataSetSort sort = new DataSetSort();
        sort.add("applyAt", SortDirection.DESC);

        Pagination pagination = new Pagination();
        pagination.setStart(0);
        pagination.setLimit(1);

        List<TariffAssignmentDataSet> assignmentList = getList(filter, sort, pagination);
        if (assignmentList.isEmpty())
            return null;
        return assignmentList.get(0).getTariff();
    }

    private List<TariffAssignmentDataSet> getListForApply(Date until){
        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.LT_EQ, until);
        filter.add("processed", CmpOperator.EQ, false);
        return getList(filter, null, null);
    }

    private void deleteOtherAssignment(Date day, long customerId){
        TariffAssignmentDataSetDao dao = daoFactory.getTariffAssignmentDao();
        Date start = startOfDay(day);
        Date end = endOfDay(day);

        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.GT_EQ, start);
        filter.add("applyAt", CmpOperator.LT_EQ, end);
        filter.add("customerId", CmpOperator.EQ, customerId);
        List<TariffAssignmentDataSet> assignmentList = dao.getList(filter, null, null);

        for (Iterator<TariffAssignmentDataSet> i = assignmentList.iterator(); i.hasNext();)
            dao.delete(i.next());
    }

    private Date startOfDay(Date date){
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        return start.getTime();
    }

    private Date endOfDay(Date date){
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.SECOND, 59);
        return start.getTime();
    }

    private void sendMsg(EventMessage message){
        EventSystem eventSystem = application.getByType(EventSystem.class);
        if (eventSystem != null)
            eventSystem.pushMessage(message);
    }
}
