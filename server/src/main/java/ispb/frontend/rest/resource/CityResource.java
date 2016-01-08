package ispb.frontend.rest.resource;

import ispb.base.Application;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.rest.ErrorRestResponse;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CityResource extends RestResource {

    private static class CityEntity extends RestEntity {

        private String name = null;

        public CityEntity(){}

        public CityEntity(CityDataSet city){
            setId(city.getId());
            setName(city.getName());
        }

        public boolean verify(){
            if (name != null)
                return true;
            return false;
        }

        public CityDataSet makeDataSet(){
            CityDataSet city = new CityDataSet();
            city.setName(getName());
            return city;
        }

        public void updateDataSet(CityDataSet city){
            city.setName(getName());
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class CityListRestResponse extends RestResponse{

        private List<CityEntity> city_list = null;

        public CityListRestResponse(List<CityDataSet> city_list){
            this.city_list = new LinkedList<CityEntity>();
            for (Iterator<CityDataSet> i = city_list.iterator(); i.hasNext(); ){
                CityEntity city = new CityEntity(i.next());
                this.city_list.add(city);
            }
        }

        public CityListRestResponse(CityDataSet city){
            this.city_list = new LinkedList<CityEntity>();
            this.city_list.add(new CityEntity(city));
        }

        public List<CityEntity> getCity_list() {
            return city_list;
        }

        public void setCity_list(List<CityEntity> city_list) {
            this.city_list = city_list;
        }
    }

    public Class getEntityType(){
        return CityEntity.class;
    }

    public RestResponse getEntity(HttpServletRequest request,
                                  HttpServletResponse response,
                                  long id,
                                  Map<String, String[]> params,
                                  Application application){
        CityDataSet city = application.getDaoFactory().getCityDao().getById(id);
        if (city == null)
            return null;
        return new CityListRestResponse(city);
    }

    public RestResponse getEntityList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Map<String, String[]> params,
                                      Application application){
        return new CityListRestResponse(application.getDaoFactory().getCityDao().getAll());
    }

    public RestResponse createEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        CityEntity entity = (CityEntity)obj;
        CityDataSetDao dao = application.getDaoFactory().getCityDao();
        if (dao.getByName(entity.getName()) != null)
            return ErrorRestResponse.alreadyExist();

        CityDataSet city = entity.makeDataSet();
        dao.save(city);
        return new CityListRestResponse(city);
    }

    public RestResponse updateEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        CityEntity entity = (CityEntity)obj;
        CityDataSetDao dao = application.getDaoFactory().getCityDao();
        CityDataSet city = dao.getById(id);
        if (city == null)
            return null; // TODO: Return error
        // TODO: Check not city exist
        entity.updateDataSet(city);
        dao.save(city);
        return new CityListRestResponse(city);
    }

    public RestResponse deleteEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     Map<String, String[]> params,
                                     Application application){
        // TODO: delete entity
        return null;
    }
}
