package ispb.base.service.account;


import ispb.base.db.container.RadiusUserAttributeContainer;
import ispb.base.db.container.RadiusUserContainer;
import ispb.base.db.dataset.RadiusUserAttributeDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface RadiusUserService {

    List<RadiusUserDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getCount(DataSetFilter filter);

    RadiusUserDataSet create(RadiusUserContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException;
    RadiusUserDataSet update(long userId, RadiusUserContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException;
    void delete(long userId) throws NotFoundException;

    RadiusUserDataSet getUserByName(String userName);
    boolean userNameExist(String userName);
    boolean ip4Exist(String ip4Address);

    List<RadiusUserAttributeDataSet> getAttributeList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    List<RadiusUserAttributeDataSet> getAttributeList(long userId, RadiusAttributeCondition condition);
    long getAttributeCount(DataSetFilter filter);
    RadiusUserAttributeDataSet createAttribute(RadiusUserAttributeContainer container) throws NotFoundException;
    RadiusUserAttributeDataSet updateAttribute(long attributeId, RadiusUserAttributeContainer container) throws NotFoundException;
    void deleteAttribute(long attributeId) throws NotFoundException;
}
