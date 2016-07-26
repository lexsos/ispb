package ispb.frontend.rest.resource;

import ispb.base.db.dataset.RadiusClientParameterDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.RadiusClientDictionaryService;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;


public class RadiusClientParameterResource extends RestResource {

    private static class RadiusClientParameterEntity extends RestEntity {

        private final long radiusClientId;
        private final String parameterName;
        private final String parameterValue;

        public RadiusClientParameterEntity(RadiusClientParameterDataSet parameter){
            setId(parameter.getId());
            this.radiusClientId = parameter.getClient().getId();
            this.parameterName = parameter.getParameter();
            this.parameterValue = parameter.getValue();
        }

        public boolean verify(){
            return getParameterName() != null && getParameterValue() != null && getRadiusClientId() > 0;
        }

        public long getRadiusClientId() {
            return radiusClientId;
        }

        public String getParameterName() {
            return parameterName;
        }

        public String getParameterValue() {
            return parameterValue;
        }
    }

    private static class RadiusClientParameterRestResponse extends RestResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<RadiusClientParameterEntity> radiusClientParameterList = new LinkedList<>();

        public RadiusClientParameterRestResponse(RadiusClientParameterDataSet parameter){
            RadiusClientParameterEntity entity = new RadiusClientParameterEntity(parameter);
            radiusClientParameterList.add(entity);
        }

        public RadiusClientParameterRestResponse(List<RadiusClientParameterDataSet> parameterList){
            for (RadiusClientParameterDataSet parameter: parameterList) {
                RadiusClientParameterEntity entity = new RadiusClientParameterEntity(parameter);
                radiusClientParameterList.add(entity);
            }
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return RadiusClientParameterEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        RadiusClientDictionaryService clientService = getClientService(restContext);
        List<RadiusClientParameterDataSet> parameters = clientService.getParameterList(restContext.getDataSetFilter(), null);
        return new RadiusClientParameterRestResponse(parameters);
    }

    public RestResponse createEntity(RestContext restContext){
        RadiusClientDictionaryService clientService = getClientService(restContext);
        RadiusClientParameterEntity entity = restContext.getEntityByType(RadiusClientParameterEntity.class);
        try {
            RadiusClientParameterDataSet parameter = clientService.createParameter(entity.getRadiusClientId(),
                    entity.getParameterName(), entity.getParameterValue());
            return new RadiusClientParameterRestResponse(parameter);
        } catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        RadiusClientDictionaryService clientService = getClientService(restContext);
        RadiusClientParameterEntity entity = restContext.getEntityByType(RadiusClientParameterEntity.class);

        try {
            RadiusClientParameterDataSet parameter = clientService.updateParameter(restContext.getId(),
                    entity.getParameterName(), entity.getParameterValue());
            return new  RadiusClientParameterRestResponse(parameter);
        }catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        RadiusClientDictionaryService clientService = getClientService(restContext);
        try {
            clientService.deleteParameter(restContext.getId());
            return new RestResponse();
        }catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("radiusClientId__eq")) {
            return new DataSetFilterItem("clientId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }

    private RadiusClientDictionaryService getClientService(RestContext restContext){
        return restContext.getApplication().getByType(RadiusClientDictionaryService.class);
    }
}
