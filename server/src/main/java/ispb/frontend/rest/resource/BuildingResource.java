package ispb.frontend.rest.resource;

import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.BuildingDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BuildingResource extends RestResource {

    private static class BuildingEntity extends RestEntity {
        private String name;
        private long cityId;
        private String cityName;
        private long streetId;
        private String streetName;
        private String qualifiedStreetName;

        public boolean verify(){
            if (name != null && streetId > 0)
                return true;
            return false;
        }

        public BuildingEntity (){}

        public BuildingEntity (BuildingDataSet building){
            setId(building.getId());
            setName(building.getName());

            setStreetId(building.getStreet().getId());
            setStreetName(building.getStreet().getName());

            setCityId(building.getStreet().getCity().getId());
            setCityName(building.getStreet().getCity().getName());

            StringBuilder fullStrName = new StringBuilder();
            fullStrName.append(getCityName());
            fullStrName.append(", ");
            fullStrName.append(getStreetName());
            setQualifiedStreetName(fullStrName.toString());
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCityId() {
            return cityId;
        }

        public void setCityId(long cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public long getStreetId() {
            return streetId;
        }

        public void setStreetId(long streetId) {
            this.streetId = streetId;
        }

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public String getQualifiedStreetName() {
            return qualifiedStreetName;
        }

        public void setQualifiedStreetName(String qualifiedStreetName) {
            this.qualifiedStreetName = qualifiedStreetName;
        }
    }

    private static class BuildingListRestResponse extends RestResponse {

        private List<BuildingEntity> buildingList = null;

        public BuildingListRestResponse(List<BuildingDataSet> buildingList){
            this.buildingList = new LinkedList();
            for (Iterator<BuildingDataSet> i = buildingList.iterator(); i.hasNext(); ){
                BuildingEntity entity = new BuildingEntity(i.next());
                this.buildingList.add(entity);
            }
        }

        public BuildingListRestResponse(BuildingDataSet building){
            this.buildingList = new LinkedList();
            BuildingEntity entity = new BuildingEntity(building);
            this.buildingList.add(entity);
        }

        public List<BuildingEntity> getBuildingList() {
            return buildingList;
        }

        public void setBuildingList(List<BuildingEntity> buildingList) {
            this.buildingList = buildingList;
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType(){
        return BuildingEntity.class;
    }


    public RestResponse getEntityList(RestContext restContext){
        BuildingDictionaryService service = getBuildingDicService(restContext);
        DataSetFilter filter = restContext.getDataSetFilter();
        DataSetSort sort = restContext.getDataSetSort();
        return new BuildingListRestResponse(service.getList(filter, sort));
    }

    public RestResponse createEntity(RestContext restContext){
        BuildingEntity entity = (BuildingEntity)restContext.getEntity();
        BuildingDictionaryService service = getBuildingDicService(restContext);
        try {
            BuildingDataSet building = service.create(entity.getStreetId(), entity.getName());
            return new BuildingListRestResponse(building);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (DicElementNotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        BuildingEntity entity = (BuildingEntity)restContext.getEntity();
        BuildingDictionaryService service = getBuildingDicService(restContext);
        try {
            BuildingDataSet building = service.update(restContext.getId(), entity.getStreetId(), entity.getName());
            return new BuildingListRestResponse(building);
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
        BuildingDictionaryService service = getBuildingDicService(restContext);
        try {
            service.delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private BuildingDictionaryService getBuildingDicService(RestContext restContext){
        return restContext.getApplication().getByType(BuildingDictionaryService.class);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        if (restItem.propertyEquals("streetId__eq")) {
            return new DataSetFilterItem("streetId", CmpOperator.EQ, restItem.asLong());
        }
        else if (restItem.propertyEquals("name__eq")){
            return new DataSetFilterItem("name", CmpOperator.EQ, restItem.getValue());
        }
        return null;
    }
}
