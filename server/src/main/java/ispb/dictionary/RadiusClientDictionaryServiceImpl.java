package ispb.dictionary;


import ispb.base.db.container.RadiusClientContainer;
import ispb.base.db.dao.RadiusClientDataSetDao;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.utils.Pagination;
import ispb.base.service.dictionary.RadiusClientDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.utils.Ip4Address;

import java.util.List;

public class RadiusClientDictionaryServiceImpl implements RadiusClientDictionaryService {

    private final DaoFactory daoFactory;

    public RadiusClientDictionaryServiceImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    public long getCount(DataSetFilter filter){
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        return dao.getCount(filter);
    }

    public List<RadiusClientDataSet> getList(DataSetFilter filter, DataSetSort sort, Pagination pagination){
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        return dao.getList(filter, sort, pagination);
    }

    public RadiusClientDataSet create(RadiusClientContainer container) throws AlreadyExistException, InvalidIpAddressException {

        if (ip4exist(container.getIp4Address()))
            throw new AlreadyExistException();

        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();

        RadiusClientDataSet client = new RadiusClientDataSet();
        update(client, container);
        dao.save(client);
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
        return client;
    }

    public void delete(long clientId) throws NotFoundException {
        RadiusClientDataSetDao dao = daoFactory.getRadiusClientDataSetDao();
        RadiusClientDataSet client = dao.getById(clientId);
        if (client == null)
            throw new NotFoundException();
        dao.delete(client);
    }

    public boolean ip4exist(String ip4Address){
        return getByIp4(ip4Address) != null;
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
}
