package ispb.dictionary;


import ispb.base.Application;
import ispb.base.db.container.RadiusClientContainer;
import ispb.base.db.dao.RadiusClientDataSetDao;
import ispb.base.db.dao.RadiusClientParameterDataSetDao;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusClientParameterDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.eventsys.EventMessage;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.RadiusClientUpdatedMsq;
import ispb.base.service.dictionary.RadiusClientDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.utils.Ip4Address;

import java.util.List;

public class RadiusClientDictionaryServiceImpl implements RadiusClientDictionaryService {

    private final DaoFactory daoFactory;
    private final Application application;

    public RadiusClientDictionaryServiceImpl(DaoFactory daoFactory, Application application){
        this.daoFactory = daoFactory;
        this.application = application;
    }

    public long getCount(DataSetFilter filter){
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        return dao.getCount(filter);
    }

    public List<RadiusClientDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public List<RadiusClientDataSet> getRadiusClientList(){
        return getList(null, null, null);
    }

    public RadiusClientDataSet create(RadiusClientContainer container) throws AlreadyExistException, InvalidIpAddressException {

        if (ip4exist(container.getIp4Address()))
            throw new AlreadyExistException();

        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();

        RadiusClientDataSet client = new RadiusClientDataSet();
        update(client, container);
        dao.save(client);
        sendMsg(new RadiusClientUpdatedMsq());
        return client;
    }

    public RadiusClientDataSet update(long clientId, RadiusClientContainer container)
            throws AlreadyExistException, NotFoundException, InvalidIpAddressException {
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();

        RadiusClientDataSet client = dao.getById(clientId);
        if (client == null)
            throw new NotFoundException();

        RadiusClientDataSet otherClient = getByIp4(container.getIp4Address());
        if (otherClient != null && client.getId() != otherClient.getId())
            throw new AlreadyExistException();

        update(client, container);
        dao.save(client);
        sendMsg(new RadiusClientUpdatedMsq());
        return client;
    }

    public void delete(long clientId) throws NotFoundException {
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        RadiusClientDataSet client = dao.getById(clientId);
        if (client == null)
            throw new NotFoundException();
        dao.delete(client);
        sendMsg(new RadiusClientUpdatedMsq());
    }

    public boolean ip4exist(String ip4Address){
        return getByIp4(ip4Address) != null;
    }

    public RadiusClientParameterDataSet createParameter(long clientId, String parameter, String value) throws NotFoundException{
        RadiusClientDataSetDao clientDao = daoFactory.getRadiusClientDataSetDao();
        RadiusClientParameterDataSetDao parameterDao = daoFactory.getRadiusClientParameterDataSetDao();

        RadiusClientDataSet client = clientDao.getById(clientId);
        if (client == null)
            throw new NotFoundException();

        RadiusClientParameterDataSet parameterDataSet = new RadiusClientParameterDataSet();
        parameterDataSet.setClient(client);
        parameterDataSet.setParameter(parameter);
        parameterDataSet.setValue(value);
        parameterDao.save(parameterDataSet);
        sendMsg(new RadiusClientUpdatedMsq());
        return parameterDataSet;
    }

    public RadiusClientParameterDataSet updateParameter(long parameterId, String parameter, String value) throws NotFoundException{
        RadiusClientParameterDataSetDao parameterDao = daoFactory.getRadiusClientParameterDataSetDao();
        RadiusClientParameterDataSet parameterDataSet = parameterDao.getById(parameterId);
        if (parameterDataSet == null)
            throw new NotFoundException();

        parameterDataSet.setParameter(parameter);
        parameterDataSet.setValue(value);
        parameterDao.save(parameterDataSet);
        sendMsg(new RadiusClientUpdatedMsq());
        return parameterDataSet;
    }

    public List<RadiusClientParameterDataSet> getParameterList(DataSetFilter filter, DataSetSort sort){
        RadiusClientParameterDataSetDao parameterDao = daoFactory.getRadiusClientParameterDataSetDao();
        return parameterDao.getList(filter, sort);
    }

    public List<RadiusClientParameterDataSet> getClientParameters(long clientId){
        DataSetFilter filter = new DataSetFilter();
        filter.add("clientId", CmpOperator.EQ, clientId);
        return getParameterList(filter, null);
    }

    public void deleteParameter(long parameterId) throws NotFoundException{
        RadiusClientParameterDataSetDao parameterDao = daoFactory.getRadiusClientParameterDataSetDao();
        RadiusClientParameterDataSet parameterDataSet = parameterDao.getById(parameterId);
        if (parameterDataSet == null)
            throw new NotFoundException();
        parameterDao.delete(parameterDataSet);
        sendMsg(new RadiusClientUpdatedMsq());
    }

    private RadiusClientDataSet getByIp4(String ip4Address){

        String ip4normalized = Ip4Address.normalize(ip4Address);
        if (ip4normalized == null)
            return null;

        DataSetFilter filter = new DataSetFilter();
        filter.add("ip4Address", CmpOperator.EQ, ip4normalized);
        List<RadiusClientDataSet> list = getList(filter, null, null);

        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    private void update(RadiusClientDataSet client, RadiusClientContainer container) throws InvalidIpAddressException {
        String ip4normalized = Ip4Address.normalize(container.getIp4Address());
        if (ip4normalized == null)
            throw new InvalidIpAddressException();

        client.setIp4Address(ip4normalized);
        client.setSecret(container.getSecret());
        client.setAddAuthRequest(container.isAddAuthRequest());
        client.setRejectInactive(container.isRejectInactive());
        client.setClientType(container.getClientType());
    }

    private void sendMsg(EventMessage message){
        EventSystem eventSystem = application.getByType(EventSystem.class);
        if (eventSystem != null)
            eventSystem.pushMessage(message);
    }
}
