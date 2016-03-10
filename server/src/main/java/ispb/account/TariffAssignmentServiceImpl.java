package ispb.account;

import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.TariffAssignmentDataSetDao;
import ispb.base.db.dao.TariffDataSetDao;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.TariffAssignmentDataSet;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.exception.BadDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TariffAssignmentServiceImpl implements TariffAssignmentService {

    private DaoFactory daoFactory;

    public TariffAssignmentServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
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

        // TODO: Need send message about new tariff assignment ?
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
}
