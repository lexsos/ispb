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

    List<RadiusUserDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);

    RadiusUserDataSet create(RadiusUserContainer container)  throws AlreadyExistException, NotFoundException;
    RadiusUserDataSet update(long userId, RadiusUserContainer container)throws AlreadyExistException, NotFoundException;
    void delete(long userId) throws NotFoundException;

    RadiusUserDataSet getUserByName(String userName);
    boolean userNameExist(String userName);
    boolean ip4Exist(String ip4Address);
}
