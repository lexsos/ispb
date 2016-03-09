package ispb.account;

import ispb.base.db.dao.CustomerDataSetDao;
import ispb.base.db.dao.PaymentDataSetDao;
import ispb.base.db.dao.PaymentGroupDataSetDao;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.dataset.PaymentGroupDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.account.PaymentService;
import ispb.base.service.exception.IncorrectDateException;
import ispb.base.service.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private DaoFactory daoFactory;

    public PaymentServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<PaymentDataSet> getPayments(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        PaymentDataSetDao dao = daoFactory.getPaymentDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getPaymentCount(DataSetFilter filter){
        PaymentDataSetDao dao = daoFactory.getPaymentDao();
        return dao.getCount(filter);
    }

    public void addVirtualPayment(long customerId, double sum, Date until, String comment)
            throws NotFoundException, IncorrectDateException{
        PaymentGroupDataSetDao paymentGroupDao = daoFactory.getPaymentGroupDao();
        PaymentDataSetDao paymentDao = daoFactory.getPaymentDao();
        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();
        Date now = new Date();

        CustomerDataSet customer = customerDao.getById(customerId);
        if (customer == null)
            throw new NotFoundException();

        if (until.before(now))
            throw new IncorrectDateException();

        PaymentGroupDataSet group = new PaymentGroupDataSet();
        group.setComment(comment);
        paymentGroupDao.save(group);

        PaymentDataSet positivePayment = new PaymentDataSet();
        positivePayment.setCustomer(customer);
        positivePayment.setApplyAt(new Date());
        positivePayment.setPaymentSum(sum);
        positivePayment.setGroup(group);
        paymentDao.save(positivePayment);

        PaymentDataSet negativePayment = new PaymentDataSet();
        negativePayment.setCustomer(customer);
        negativePayment.setApplyAt(until);
        negativePayment.setPaymentSum(-sum);
        negativePayment.setGroup(group);
        paymentDao.save(negativePayment);

        // TODO: send event message about new payment
    }

    public void addPayment(long customerId, double sum, String comment)
            throws NotFoundException{
        PaymentGroupDataSetDao paymentGroupDao = daoFactory.getPaymentGroupDao();
        PaymentDataSetDao paymentDao = daoFactory.getPaymentDao();
        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();

        CustomerDataSet customer = customerDao.getById(customerId);
        if (customer == null)
            throw new NotFoundException();

        PaymentGroupDataSet group = new PaymentGroupDataSet();
        group.setComment(comment);
        paymentGroupDao.save(group);

        PaymentDataSet payment = new PaymentDataSet();
        payment.setCustomer(customer);
        payment.setApplyAt(new Date());
        payment.setPaymentSum(sum);
        payment.setGroup(group);
        paymentDao.save(payment);

        // TODO: send event message about new payment
    }

    public List<PaymentGroupDataSet> getPaymentGroups(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        PaymentGroupDataSetDao dao = daoFactory.getPaymentGroupDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getPaymentGroupCount(DataSetFilter filter){
        PaymentGroupDataSetDao dao = daoFactory.getPaymentGroupDao();
        return dao.getCount(filter);
    }
}
