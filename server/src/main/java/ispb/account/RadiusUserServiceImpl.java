package ispb.account;


import ispb.base.Application;
import ispb.base.db.container.RadiusUserContainer;
import ispb.base.db.dao.RadiusUserDataSetDao;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.utils.Ip4Address;

import java.util.List;

public class RadiusUserServiceImpl implements RadiusUserService {

    private final DaoFactory daoFactory;
    private final Application application;

    public RadiusUserServiceImpl(DaoFactory daoFactory, Application application){
        this.daoFactory = daoFactory;
        this.application = application;
    }

    public List<RadiusUserDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusUserDataSetDao dao = daoFactory.getRadiusUserDataSetDao();
        return dao.getList(filter, sort, pagination);

    }

    public long getCount(DataSetFilter filter){
        RadiusUserDataSetDao dao = daoFactory.getRadiusUserDataSetDao();
        return dao.getCount(filter);
    }

    public RadiusUserDataSet create(RadiusUserContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException {
        if (userNameExist(container.getUserName()) || ip4Exist(container.getIp4Address()))
            throw new AlreadyExistException();

        RadiusUserDataSetDao userDao = daoFactory.getRadiusUserDataSetDao();

        RadiusUserDataSet radiusUser = new RadiusUserDataSet();
        update(radiusUser, container);
        userDao.save(radiusUser);
        return radiusUser;
    }

    public RadiusUserDataSet update(long userId, RadiusUserContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException{
        RadiusUserDataSetDao userDao = daoFactory.getRadiusUserDataSetDao();

        RadiusUserDataSet radiusUser = userDao.getById(userId);
        if (radiusUser == null)
            throw new NotFoundException();

        RadiusUserDataSet otherRadiusUser = getUserByName(container.getUserName());
        if (otherRadiusUser != null && otherRadiusUser.getId() != radiusUser.getId())
            throw new AlreadyExistException();
        if (container.getIp4Address() != null) {
            otherRadiusUser = getUserByIp4(container.getIp4Address());
            if (otherRadiusUser != null && otherRadiusUser.getId() != radiusUser.getId())
                throw new AlreadyExistException();
        }

        update(radiusUser, container);
        userDao.save(radiusUser);
        return radiusUser;

    }

    public void delete(long userId) throws NotFoundException{
        RadiusUserDataSetDao userDao = daoFactory.getRadiusUserDataSetDao();
        RadiusUserDataSet radiusUser = userDao.getById(userId);
        if (radiusUser == null)
            throw new NotFoundException();
        userDao.delete(radiusUser);
    }

    public RadiusUserDataSet getUserByName(String userName){
        DataSetFilter filter = new DataSetFilter();
        filter.add("userName", CmpOperator.EQ, userName);
        List<RadiusUserDataSet> users = getList(filter, null, null);
        if (!users.isEmpty())
            return users.get(0);
        return null;
    }

    public boolean userNameExist(String userName){
        return getUserByName(userName) != null;
    }

    public boolean ip4Exist(String ip4Address){
        return getUserByIp4(ip4Address) != null;
    }

    private RadiusUserDataSet getUserByIp4(String ip4Address){
        String normalizedIp = Ip4Address.normalize(ip4Address);
        if (normalizedIp == null)
            return null;
        DataSetFilter filter = new DataSetFilter();
        filter.add("ip4Address", CmpOperator.EQ, normalizedIp);
        List<RadiusUserDataSet> users = getList(filter, null, null);
        if (!users.isEmpty())
            return users.get(0);
        return null;
    }

    private CustomerAccountService getCustomerService(){
        return application.getByType(CustomerAccountService.class);
    }

    private void update(RadiusUserDataSet radiusUser, RadiusUserContainer container)
            throws NotFoundException, InvalidIpAddressException {

        radiusUser.setUserName(container.getUserName());
        radiusUser.setPassword(container.getPassword());

        if (container.getIp4Address() != null && container.getIp4Address().length() > 0) {
            String ip4Address = Ip4Address.normalize(container.getIp4Address());
            if (ip4Address == null)
                throw new InvalidIpAddressException();
            radiusUser.setIp4Address(ip4Address);
        }
        else
            radiusUser.setIp4Address(null);

        if (container.getCustomerId() != null) {
            CustomerAccountService customerService = getCustomerService();
            CustomerSummeryView customerSummery = customerService.getSummeryById(container.getCustomerId());
            if (customerSummery == null)
                throw new NotFoundException();
            else
                radiusUser.setCustomer(customerSummery.getCustomer());
        }
    }
}
