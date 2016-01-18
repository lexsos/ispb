package ispb.frontend.rest.resource;


import ispb.base.Application;
import ispb.base.db.dataset.BuildingDataSet;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.BuildingDictionaryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BuildingResource extends RestResource {

    private static class BuildingEntity extends RestEntity {
        private String name;
        private long cityId;
        private String cityName;
        private long streetId;
        private String streetName;

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


    public RestResponse getEntityList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Map<String, String[]> params,
                                      Application application){
        return new BuildingListRestResponse(getBuildingDicService(application).getAll());
    }

    private BuildingDictionaryService getBuildingDicService(Application application){
        return application.getByType(BuildingDictionaryService.class);
    }
}
