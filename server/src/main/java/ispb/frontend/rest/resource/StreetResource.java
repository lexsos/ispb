package ispb.frontend.rest.resource;

import ispb.base.Application;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.frontend.rest.ErrorRestResponse;
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

        public StreetListRestResponse(StreetDataSet street){
            streetList = new LinkedList<StreetEntity>();
            streetList.add(new  StreetEntity(street));
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

    public RestResponse createEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        StreetEntity entity = (StreetEntity)obj;
        StreetDataSetDao streetDao = application.getDaoFactory().getStreetDao();
        CityDataSetDao cityDao = application.getDaoFactory().getCityDao();

        CityDataSet city = cityDao.getById(entity.getCityId());

        if (city == null)
            return ErrorRestResponse.notFound();

        if (streetDao.getByName(city, entity.getName()) != null)
            return ErrorRestResponse.alreadyExist();

        StreetDataSet street = new StreetDataSet(entity.getName(), city);
        streetDao.save(street);

        return new StreetListRestResponse(street);
    }

    public RestResponse updateEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        StreetEntity entity = (StreetEntity)obj;
        StreetDataSetDao streetDao = application.getDaoFactory().getStreetDao();
        CityDataSetDao cityDao = application.getDaoFactory().getCityDao();
        StreetDataSet street = streetDao.getById(id);

        if (street == null)
            return ErrorRestResponse.notFound();

        CityDataSet city = cityDao.getById(entity.getCityId());

        if (city == null)
            return ErrorRestResponse.notFound();

        if (streetDao.getByName(city, entity.getName()) != null)
            return ErrorRestResponse.alreadyExist();

        street.setName(entity.getName());
        street.setCity(city);
        streetDao.save(street);

        return new StreetListRestResponse(street);
    }

    public RestResponse deleteEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     Map<String, String[]> params,
                                     Application application){
        StreetDataSetDao dao = application.getDaoFactory().getStreetDao();
        StreetDataSet street = dao.getById(id);

        if (street == null)
            return ErrorRestResponse.notFound();;

        dao.delete(street);
        return new RestResponse();
    }
}
