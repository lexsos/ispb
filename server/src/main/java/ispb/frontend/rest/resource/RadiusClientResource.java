package ispb.frontend.rest.resource;


import ispb.base.db.container.RadiusClientContainer;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.fieldtype.RadiusClientType;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.RadiusClientDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;

public class RadiusClientResource extends RestResource {

    private static class RadiusClientEntity extends RestEntity implements RadiusClientContainer {

        private final String ip4Address;
        private final String secret;
        private final boolean rejectInactive;
        private final RadiusClientType clientType;

        public RadiusClientEntity(RadiusClientDataSet client){
            setId(client.getId());
            ip4Address = client.getIp4Address();
            secret = client.getSecret();
            rejectInactive = client.isRejectInactive();
            clientType = client.getClientType();
        }

        public boolean verify(){
            return ip4Address != null && secret != null && clientType != null;
        }

        public String getIp4Address(){
            return ip4Address;
        }

        public String getSecret(){
            return secret;
        }

        public boolean isRejectInactive(){
            return rejectInactive;
        }

        public RadiusClientType getClientType(){
            return clientType;
        }
    }

    private static class RadiusClientRestResponse extends RestResponse {

        private final long total;

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<RadiusClientEntity> radiusClientList = new LinkedList<>();

        public RadiusClientRestResponse(RadiusClientDataSet client){
            total = 0;
            radiusClientList.add(new RadiusClientEntity(client));
        }

        @SuppressWarnings("Convert2streamapi")
        public RadiusClientRestResponse(List<RadiusClientDataSet> clientSet, long total){
            this.total = total;
            for (RadiusClientDataSet client: clientSet)
                radiusClientList.add(new RadiusClientEntity(client));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return RadiusClientEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        RadiusClientDictionaryService service = getRadiusClientService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        Pagination pagination = restContext.getPagination();

        List<RadiusClientDataSet> clientList = service.getList(filter, sort, pagination);
        long total = service.getCount(filter);

        return new RadiusClientRestResponse(clientList, total);
    }

    public RestResponse createEntity(RestContext restContext){
        RadiusClientDictionaryService service = getRadiusClientService(restContext);
        RadiusClientEntity entity = restContext.getEntityByType(RadiusClientEntity.class);

        try {
            RadiusClientDataSet client = service.create(entity);
            return new RadiusClientRestResponse(client);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (InvalidIpAddressException e){
            return ErrorRestResponse.incompatibleDataStructure();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        RadiusClientDictionaryService service = getRadiusClientService(restContext);
        RadiusClientEntity entity = restContext.getEntityByType(RadiusClientEntity.class);

        try {
            RadiusClientDataSet client = service.update(restContext.getId(), entity);
            return new RadiusClientRestResponse(client);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (InvalidIpAddressException e){
            return ErrorRestResponse.incompatibleDataStructure();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        RadiusClientDictionaryService service = getRadiusClientService(restContext);

        try {
            service.delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }


    private RadiusClientDictionaryService getRadiusClientService(RestContext restContext){
        return restContext.getApplication().getByType(RadiusClientDictionaryService.class);
    }
}
