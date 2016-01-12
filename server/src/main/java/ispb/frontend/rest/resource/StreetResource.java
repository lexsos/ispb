package ispb.frontend.rest.resource;

import ispb.base.Application;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            if (name != null && cityId > 0)
                return true;
            return false;
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

    private static class StreetListRestResponse extends RestResponse {

        private List<StreetEntity> streetList = null;

        public StreetListRestResponse(List<StreetDataSet> streetList){
            this.streetList = new LinkedList<StreetEntity>();
            for (Iterator<StreetDataSet> i = streetList.iterator(); i.hasNext(); )
                this.streetList.add(new StreetEntity(i.next()));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType(){
        return StreetEntity.class;
    }

    public RestResponse getEntityList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Map<String, String[]> params,
                                      Application application){
        return new StreetListRestResponse(application.getDaoFactory().getStreetDao().getAll());
    }
}
