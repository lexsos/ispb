package ispb.account;

import ispb.base.db.dao.CustomerSummeryViewDao;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.account.CustomerAccountService;

import java.util.List;

public class CustomerAccountServiceImpl implements CustomerAccountService {

    private DaoFactory daoFactory;

    public CustomerAccountServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<CustomerSummeryView> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getCount(DataSetFilter filter){
        CustomerSummeryViewDao dao = daoFactory.getCustomerSummeryViewDao();
        return dao.getCount(filter);
    }
}
