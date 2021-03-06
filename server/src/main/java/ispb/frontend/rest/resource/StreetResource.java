package ispb.frontend.rest.resource;

import ispb.base.db.dataset.StreetDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.StreetDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.*;

public class StreetResource extends RestResource {

    private static class StreetEntity extends RestEntity {

        private String name = null;
        private String cityName = null;
        private long cityId = -1;

        public StreetEntity(){}

        public StreetEntity(StreetDataSet street){
            setId(street.getId());
            setName(street.getName());
            setCityName(street.getCity().getName());
            setCityId(street.getCity().getId());
        }

        public boolean verify(){
            return name != null && cityId > 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public long getCityId() {
            return cityId;
        }

        public void setCityId(long cityId) {
            this.cityId = cityId;
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class StreetListRestResponse extends RestResponse {

        private final List<StreetEntity> streetList = new LinkedList<>();

        @SuppressWarnings("Convert2streamapi")
        public StreetListRestResponse(List<StreetDataSet> streetList){
            for (StreetDataSet streetDataSet : streetList)
                this.streetList.add(new StreetEntity(streetDataSet));
        }

        public StreetListRestResponse(StreetDataSet street){
            streetList.add(new  StreetEntity(street));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return StreetEntity.class;
    }

    public RestResponse getEntityList(RestContext restContext){
        StreetDictionaryService service = getStreetDicService(restContext);
        return new StreetListRestResponse(service.getList(restContext.getDataSetFilter()));
    }

    public RestResponse createEntity(RestContext restContext){
        StreetEntity entity = restContext.getEntityByType(StreetEntity.class);
        StreetDictionaryService service = getStreetDicService(restContext);
        try {
            StreetDataSet street = service.create(entity.getCityId(), entity.getName());
            return new StreetListRestResponse(street);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (DicElementNotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        StreetEntity entity = restContext.getEntityByType(StreetEntity.class);
        StreetDictionaryService service = getStreetDicService(restContext);
        try {
            StreetDataSet street = service.update(restContext.getId(), entity.getCityId(), entity.getName());
            return new StreetListRestResponse(street);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (DicElementNotFoundException e){
            return ErrorRestResponse.notFound();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        try {
            getStreetDicService(restContext).delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private StreetDictionaryService getStreetDicService(RestContext restContext){
        return restContext.getApplication().getByType(StreetDictionaryService.class);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("cityId__eq")) {
            return new DataSetFilterItem("cityId", CmpOperator.EQ, restItem.asLong());
        }
        else if (restItem.propertyEquals("name__like")){
            return new DataSetFilterItem("name", CmpOperator.LIKE, restItem.getValue());
        }
        else if (restItem.propertyEquals("name__eq")){
            return new DataSetFilterItem("name", CmpOperator.EQ, restItem.getValue());
        }

        return null;
    }
}
