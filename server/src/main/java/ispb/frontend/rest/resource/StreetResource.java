package ispb.frontend.rest.resource;

import ispb.base.Application;
import ispb.base.db.dataset.StreetDataSet;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.StreetDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.DicElementNotFoundException;
import ispb.base.service.exception.NotFoundException;

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
        StreetDictionaryService service = getStreetDicService(application);
        RestFilter filter = getFilter(params);

        if (filter != null && filter.getCount() == 1){
            String property = filter.getItem(0).getProperty();
            String value = filter.getItem(0).getValue();
            if (property.equals("cityId"))
                return new StreetListRestResponse(service.getByCity(Long.parseLong(value)));
        }

        return new StreetListRestResponse(service.getAll());
    }

    public RestResponse createEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        StreetEntity entity = (StreetEntity)obj;
        StreetDictionaryService service = getStreetDicService(application);
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

    public RestResponse updateEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     RestEntity obj,
                                     Map<String, String[]> params,
                                     Application application){
        StreetEntity entity = (StreetEntity)obj;
        StreetDictionaryService service = getStreetDicService(application);
        try {
            StreetDataSet street = service.update(id, entity.getCityId(), entity.getName());
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

    public RestResponse deleteEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     Map<String, String[]> params,
                                     Application application){
        try {
            getStreetDicService(application).delete(id);
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private StreetDictionaryService getStreetDicService(Application application){
        return application.getByType(StreetDictionaryService.class);
    }
}
