package ispb.frontend.rest.resource;

import ispb.base.Application;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.rest.ErrorRestResponse;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class CityListRestResponse extends RestResponse{

        private List<CityEntity> city_list = null;

        public CityListRestResponse(List<CityDataSet> cityList){
            this.city_list = new LinkedList<CityEntity>();
            for (Iterator<CityDataSet> i = cityList.iterator(); i.hasNext(); ){
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

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getEntityType(){
        return CityEntity.class;
    }

    public RestResponse getEntity(HttpServletRequest request,
                                  HttpServletResponse response,
                                  long id,
                                  Map<String, String[]> params,
                                  Application application){
        CityDataSet city = getCityDicService(application).getById(id);
        if (city == null)
            return ErrorRestResponse.notFound();
        return new CityListRestResponse(city);
    }

    public RestResponse getEntityList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Map<String, String[]> params,
                                      Application application,
                                      DataSetFilter dataSetFilter,
                                      DataSetSort dataSetSort){
        return new CityListRestResponse(getCityDicService(application).getAll());
    }

    public RestResponse createEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        CityEntity entity = (CityEntity)obj;
        try {
            CityDataSet city = getCityDicService(application).create(entity.getName());
            return new CityListRestResponse(city);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
    }

    public RestResponse updateEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        CityEntity entity = (CityEntity)obj;
        try {
            CityDataSet city = getCityDicService(application).update(id, entity.getName());
            return new CityListRestResponse(city);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     Map<String, String[]> params,
                                     Application application){
        try {
            getCityDicService(application).delete(id);
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private CityDictionaryService getCityDicService(Application application){
        return application.getByType(CityDictionaryService.class);
    }
}
