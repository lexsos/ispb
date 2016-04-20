package ispb.base.service.account;


import ispb.base.db.container.RadiusUserContainer;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface RadiusUserService {

    List<RadiusUserDataSet> getUserList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getUserCount(DataSetFilter filter);

    List<RadiusUserDataSet> getUnRegList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getUnRegCount(DataSetFilter filter);

    RadiusUserDataSet create(RadiusUserContainer container)  throws AlreadyExistException;
    RadiusUserDataSet update(long userId, RadiusUserContainer container)throws AlreadyExistException, NotFoundException;
    void delete(long userId) throws NotFoundException;

    RadiusUserDataSet getUserByName(String userName);
    boolean userNameExist(String userName);
}
