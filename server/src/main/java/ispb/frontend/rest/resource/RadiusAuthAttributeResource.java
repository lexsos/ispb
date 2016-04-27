package ispb.frontend.rest.resource;

import ispb.base.db.container.RadiusUserAttributeContainer;
import ispb.base.db.dataset.RadiusUserAttributeDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;


public class RadiusAuthAttributeResource extends RestResource {

    private static class TariffRadiusAttributeEntity extends RestEntity implements RadiusUserAttributeContainer {

        private final long userId;
        private final String attributeName;
        private final String attributeValue;
        private final RadiusAttributeCondition condition;

        public TariffRadiusAttributeEntity(RadiusUserAttributeDataSet attribute){
            setId(attribute.getId());
            userId = attribute.getUserId();
            attributeName = attribute.getAttributeName();
            attributeValue = attribute.getAttributeValue();
            condition = attribute.getCondition();
        }

        public boolean verify(){
            return userId > 0 && attributeName != null && attributeValue != null && condition != null;
        }

        public long getUserId() {
            return userId;
        }

        public String getAttributeName(){
            return attributeName;
        }

        public String getAttributeValue(){
            return attributeValue;
        }

        public RadiusAttributeCondition getCondition(){
            return condition;
        }
    }

    private static class TariffRadiusAttributeRestResponse extends RestResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<TariffRadiusAttributeEntity> radiusAuthAttributeList = new LinkedList<>();

        public TariffRadiusAttributeRestResponse(RadiusUserAttributeDataSet attribute){
            radiusAuthAttributeList.add(new TariffRadiusAttributeEntity(attribute));
        }

        @SuppressWarnings("Convert2streamapi")
        public TariffRadiusAttributeRestResponse(List<RadiusUserAttributeDataSet> attributeList){
            for (RadiusUserAttributeDataSet attribute: attributeList)
                radiusAuthAttributeList.add(new TariffRadiusAttributeEntity(attribute));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return TariffRadiusAttributeEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        List<RadiusUserAttributeDataSet> attributeList = service.getAttributeList(filter, sort, null);
        return new TariffRadiusAttributeRestResponse(attributeList);
    }

    public RestResponse createEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        TariffRadiusAttributeEntity entity = restContext.getEntityByType(TariffRadiusAttributeEntity.class);
        try {
            RadiusUserAttributeDataSet attribute = service.createAttribute(entity);
            return new TariffRadiusAttributeRestResponse(attribute);
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        TariffRadiusAttributeEntity entity = restContext.getEntityByType(TariffRadiusAttributeEntity.class);
        try {
            RadiusUserAttributeDataSet attribute = service.updateAttribute(restContext.getId(), entity);
            return new TariffRadiusAttributeRestResponse(attribute);
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        RadiusUserService service = getRadiusUserService(restContext);
        try {
            service.deleteAttribute(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("userId__eq")) {
            return new DataSetFilterItem("userId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }

    private RadiusUserService getRadiusUserService(RestContext restContext){
        return restContext.getApplication().getByType(RadiusUserService.class);
    }
}
