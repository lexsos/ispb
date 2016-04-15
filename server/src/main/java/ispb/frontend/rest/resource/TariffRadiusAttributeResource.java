package ispb.frontend.rest.resource;


import ispb.base.db.container.TariffRadiusAttributeContainer;
import ispb.base.db.dataset.TariffRadiusAttributeDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;

public class TariffRadiusAttributeResource extends RestResource {

    private static class TariffRadiusAttributeEntity extends RestEntity implements TariffRadiusAttributeContainer {

        private final long tariffId;
        private final String attributeName;
        private final String attributeValue;
        private final RadiusAttributeCondition condition;

        public TariffRadiusAttributeEntity(TariffRadiusAttributeDataSet attribute){
            setId(attribute.getId());
            tariffId = attribute.getTariffId();
            attributeName = attribute.getAttributeName();
            attributeValue = attribute.getAttributeValue();
            condition = attribute.getCondition();
        }

        public boolean verify(){
            return getTariffId() > 0 && getAttributeName() != null && getAttributeValue() != null && getCondition() != null;
        }

        @Override
        public long getTariffId() {
            return tariffId;
        }

        @Override
        public String getAttributeName() {
            return attributeName;
        }

        @Override
        public String getAttributeValue() {
            return attributeValue;
        }

        @Override
        public RadiusAttributeCondition getCondition() {
            return condition;
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public static class TariffRadiusAttributeRestResponse extends RestResponse {

        private final List<TariffRadiusAttributeEntity> tariffAttributeList = new LinkedList<>();

        @SuppressWarnings("Convert2streamapi")
        public TariffRadiusAttributeRestResponse(List<TariffRadiusAttributeDataSet> dataSet){
            for (TariffRadiusAttributeDataSet attribute: dataSet)
                tariffAttributeList.add(new TariffRadiusAttributeEntity(attribute));
        }

        public TariffRadiusAttributeRestResponse(TariffRadiusAttributeDataSet attribute){
            tariffAttributeList.add(new TariffRadiusAttributeEntity(attribute));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType() {
        return TariffRadiusAttributeEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        List<TariffRadiusAttributeDataSet> attributeList = service.getAttributeList(filter, sort, null);
        return new TariffRadiusAttributeRestResponse(attributeList);
    }

    public RestResponse createEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        TariffRadiusAttributeEntity entity = restContext.getEntityByType(TariffRadiusAttributeEntity.class);
        try {
            TariffRadiusAttributeDataSet attribute = service.createAttribute(entity);
            return new TariffRadiusAttributeRestResponse(attribute);
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        try {
            service.deleteAttribute(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        TariffDictionaryService service = getTariffDicService(restContext);
        TariffRadiusAttributeEntity entity = restContext.getEntityByType(TariffRadiusAttributeEntity.class);
        try {
            TariffRadiusAttributeDataSet attribute = service.updateAttribute(restContext.getId(), entity);
            return new TariffRadiusAttributeRestResponse(attribute);
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("tariffId__eq")) {
            return new DataSetFilterItem("tariffId", CmpOperator.EQ, restItem.asLong());
        }
        return null;
    }

    private TariffDictionaryService getTariffDicService(RestContext restContext){
        return restContext.getApplication().getByType(TariffDictionaryService.class);
    }
}
