package ispb.account;

import ispb.base.db.container.RadiusSessionAttributeContainer;
import ispb.base.db.container.RadiusSessionContainer;
import ispb.base.db.dao.*;
import ispb.base.db.dataset.*;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.field.SortDirection;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.utils.DateUtils;
import ispb.base.utils.Ip4Address;

import java.util.Date;
import java.util.List;


public class RadiusSessionServiceImpl implements RadiusSessionService {

    private final DaoFactory daoFactory;

    public RadiusSessionServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public List<RadiusSessionDataSet> getSessionList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusSessionDataSetDao dao = daoFactory.getRadiusSessionDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getSessionCount(DataSetFilter filter){
        RadiusSessionDataSetDao dao = daoFactory.getRadiusSessionDataSetDao();
        return dao.getCount(filter);
    }

    public RadiusSessionDataSet createSession(RadiusSessionContainer container) throws NotFoundException{
        RadiusSessionDataSetDao dao = daoFactory.getRadiusSessionDataSetDao();
        RadiusSessionDataSet sessionDataSet = new RadiusSessionDataSet();
        updateSession(sessionDataSet, container);
        dao.save(sessionDataSet);
        return sessionDataSet;
    }

    public RadiusSessionDataSet updateSession(long sessionId, RadiusSessionContainer container) throws NotFoundException{
        RadiusSessionDataSetDao dao = daoFactory.getRadiusSessionDataSetDao();
        RadiusSessionDataSet session = dao.getById(sessionId);
        if (session == null)
            throw new NotFoundException();
        updateSession(session, container);
        dao.save(session);
        return session;
    }

    public void deleteSession(long sessionId) throws NotFoundException{
        RadiusSessionDataSetDao dao = daoFactory.getRadiusSessionDataSetDao();
        RadiusSessionDataSet session = dao.getById(sessionId);
        if (session == null)
            throw new NotFoundException();
        dao.delete(session);
    }

    public List<RadiusSessionIpDataSet> getIpList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusSessionIpDataSetDao dao = daoFactory.getRadiusSessionIpDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getIpCount(DataSetFilter filter){
        RadiusSessionIpDataSetDao dao = daoFactory.getRadiusSessionIpDataSetDao();
        return dao.getCount(filter);
    }

    public RadiusSessionIpDataSet addSessionIp(long sessionId, String ipAddress)
            throws NotFoundException, InvalidIpAddressException{
        RadiusSessionDataSetDao sessionDao = daoFactory.getRadiusSessionDataSetDao();
        RadiusSessionIpDataSetDao ipDao = daoFactory.getRadiusSessionIpDataSetDao();

        RadiusSessionDataSet session = sessionDao.getById(sessionId);
        if (session == null)
            throw new NotFoundException();


        String ip = normalizeIp(ipAddress);
        if (ip == null)
            throw new InvalidIpAddressException();

        RadiusSessionIpDataSet sessionIp = new RadiusSessionIpDataSet();
        sessionIp.setSession(session);
        sessionIp.setIpAddress(ip);
        ipDao.save(sessionIp);
        return sessionIp;
    }

    public RadiusSessionIpDataSet updateSessionIp(long sessionIpId, String ipAddress)
            throws NotFoundException, InvalidIpAddressException{
        RadiusSessionIpDataSetDao ipDao = daoFactory.getRadiusSessionIpDataSetDao();

        RadiusSessionIpDataSet sessionIp = ipDao.getById(sessionIpId);
        if (sessionIp == null)
            throw new NotFoundException();

        String ip = normalizeIp(ipAddress);
        if (ip == null)
            throw new InvalidIpAddressException();

        sessionIp.setIpAddress(ip);
        ipDao.save(sessionIp);
        return sessionIp;
    }

    public void deleteIp(long sessionIpId) throws NotFoundException{
        RadiusSessionIpDataSetDao ipDao = daoFactory.getRadiusSessionIpDataSetDao();
        RadiusSessionIpDataSet sessionIp = ipDao.getById(sessionIpId);
        if (sessionIp == null)
            throw new NotFoundException();
        ipDao.delete(sessionIp);
    }

    public List<RadiusSessionAttributeDataSet> getAttributeList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusSessionAttributeDataSetDao dao = daoFactory.getRadiusSessionAttributeDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public long getAttributeCount(DataSetFilter filter){
        RadiusSessionAttributeDataSetDao dao = daoFactory.getRadiusSessionAttributeDataSetDao();
        return dao.getCount(filter);
    }

    public RadiusSessionAttributeDataSet addAttribute(RadiusSessionAttributeContainer container)
            throws NotFoundException{
        RadiusSessionAttributeDataSetDao dao = daoFactory.getRadiusSessionAttributeDataSetDao();
        RadiusSessionAttributeDataSet attribute = new RadiusSessionAttributeDataSet();
        updateAttribute(attribute, container);
        dao.save(attribute);
        return attribute;
    }

    public RadiusSessionAttributeDataSet updateAttribute(long sessionAttributeId, RadiusSessionAttributeContainer container)
            throws NotFoundException{
        RadiusSessionAttributeDataSetDao dao = daoFactory.getRadiusSessionAttributeDataSetDao();
        RadiusSessionAttributeDataSet attribute = dao.getById(sessionAttributeId);
        if (attribute == null)
            throw new NotFoundException();
        updateAttribute(attribute, container);
        dao.save(attribute);
        return attribute;
    }

    public void deleteAttribute(long sessionAttributeId) throws NotFoundException{
        RadiusSessionAttributeDataSetDao dao = daoFactory.getRadiusSessionAttributeDataSetDao();
        RadiusSessionAttributeDataSet attribute = dao.getById(sessionAttributeId);
        if (attribute == null)
            throw new NotFoundException();
        dao.delete(attribute);
    }

    // TODO: change find algorithm for dual stack (IPv4 + IPv6)
    public CustomerDataSet getCustomerByIp(String ipAddress) throws InvalidIpAddressException{
        String ip = normalizeIp(ipAddress);
        if (ip == null)
            return null;

        DataSetFilter filter = new DataSetFilter();
        filter.add("ipAddress", CmpOperator.EQ, ipAddress);
        DataSetSort sort = new DataSetSort();
        sort.add("createAt", SortDirection.DESC);

        Pagination pagination = new Pagination();
        pagination.setLimit(1);
        pagination.setStart(0);

        List<RadiusSessionIpDataSet> ipList = getIpList(filter, sort, pagination);
        if (ipList.isEmpty())
            return null;

        RadiusSessionIpDataSet ipDataSet = ipList.get(0);
        Date now = new Date();

        Date stopAt = ipDataSet.getSession().getStopAt();
        if (stopAt != null && stopAt.after(now))
            return ipDataSet.getSession().getCustomer();

        Date expireAt = ipDataSet.getSession().getExpireAt();
        if (expireAt != null && expireAt.after(now))
            return ipDataSet.getSession().getCustomer();

        if (stopAt == null && expireAt == null)
            return ipDataSet.getSession().getCustomer();

        return null;
    }

    public long getSessionInPeriod(RadiusUserDataSet user, int periodSeconds){
        DataSetFilter filter = new DataSetFilter();
        Date date = DateUtils.subSecond(new Date(), periodSeconds);
        filter.add("radiusUserId", CmpOperator.EQ, user.getId());
        filter.add("startAt", CmpOperator.GT_EQ, date);
        return getSessionCount(filter);
    }

    private void updateSession(RadiusSessionDataSet session, RadiusSessionContainer container) throws NotFoundException {
        CustomerDataSetDao customerDao = daoFactory.getCustomerDao();
        CustomerDataSet customer = customerDao.getById(container.getCustomerId());
        if (customer == null)
            throw new NotFoundException();

        RadiusUserDataSetDao radiusUserDao = daoFactory.getRadiusUserDataSetDao();
        RadiusUserDataSet radiusUser = radiusUserDao.getById(container.getRadiusUserId());
        if (radiusUser == null)
            throw new NotFoundException();

        RadiusClientDataSetDao radiusClientDao = daoFactory.getRadiusClientDataSetDao();
        RadiusClientDataSet radiusClient = radiusClientDao.getById(container.getRadiusClientId());
        if (radiusClient == null)
            throw new NotFoundException();

        session.setCustomer(customer);
        session.setRadiusUser(radiusUser);
        session.setRadiusClient(radiusClient);
        session.setExpireAt(container.getExpireAt());
        session.setStartAt(container.getStartAt());
        session.setStopAt(container.getStopAt());
    }

    private void updateAttribute(RadiusSessionAttributeDataSet attribute, RadiusSessionAttributeContainer container)
            throws NotFoundException {

        RadiusSessionDataSetDao sessionDao = daoFactory.getRadiusSessionDataSetDao();
        RadiusSessionDataSet session = sessionDao.getById(container.getSessionId());

        if (session == null)
            throw new NotFoundException();

        attribute.setSession(session);
        attribute.setPacketAt(container.getPacketAt());
        attribute.setPacketType(container.getPacketType());
        attribute.setAttribute(container.getAttribute());
        attribute.setValue(container.getValue());
    }

    private String normalizeIp(String ipAddress){
        // TODO: Change normalize algorithm for IPv6
        return Ip4Address.normalize(ipAddress);
    }
}
