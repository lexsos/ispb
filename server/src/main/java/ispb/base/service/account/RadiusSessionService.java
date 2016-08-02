package ispb.base.service.account;


import ispb.base.db.container.RadiusSessionAttributeContainer;
import ispb.base.db.container.RadiusSessionContainer;
import ispb.base.db.dataset.*;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;

import java.util.List;

public interface RadiusSessionService {

    List<RadiusSessionDataSet> getSessionList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getSessionCount(DataSetFilter filter);
    RadiusSessionDataSet createSession(RadiusSessionContainer container) throws NotFoundException;
    RadiusSessionDataSet updateSession(long sessionId, RadiusSessionContainer container) throws NotFoundException;
    void deleteSession(long sessionId) throws NotFoundException;

    List<RadiusSessionIpDataSet> getIpList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getIpCount(DataSetFilter filter);
    RadiusSessionIpDataSet addSessionIp(long sessionId, String ipAddress)
            throws NotFoundException, InvalidIpAddressException;
    RadiusSessionIpDataSet updateSessionIp(long sessionIpId, String ipAddress)
            throws NotFoundException, InvalidIpAddressException;
    void deleteIp(long sessionIpId) throws NotFoundException;

    List<RadiusSessionAttributeDataSet> getAttributeList(DataSetFilter filter, DataSetSort sort, Pagination pagination);
    long getAttributeCount(DataSetFilter filter);
    RadiusSessionAttributeDataSet addAttribute(RadiusSessionAttributeContainer container) throws NotFoundException;
    RadiusSessionAttributeDataSet updateAttribute(long sessionAttributeId, RadiusSessionAttributeContainer container)
            throws NotFoundException;
    void deleteAttribute(long sessionAttributeId) throws NotFoundException;


    CustomerDataSet getCustomerByIp(String ipAddress) throws InvalidIpAddressException;
    long getSessionInPeriod(RadiusUserDataSet user, int periodSeconds);

    String getSessionPattern(RadiusSessionDataSet session);
    RadiusSessionDataSet getSessionByPattern(String pattern);
}
