package ispb.account;

import ispb.base.db.dao.PaymentDataSetDao;
import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.account.PaymentService;

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
}
