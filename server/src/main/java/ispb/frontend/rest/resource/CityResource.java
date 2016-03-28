package ispb.frontend.rest.resource;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.rest.*;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.CityDictionaryService;
import ispb.base.service.exception.AlreadyExistException;
import ispb.base.service.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;


public class CityResource extends RestResource {

    private static class CityEntity extends RestEntity {

        private String name = null;

        public CityEntity(){}

        public CityEntity(CityDataSet city){
            setId(city.getId());
            setName(city.getName());
        }

        public boolean verify(){
            return name != null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class CityListRestResponse extends RestResponse{

        private final List<CityEntity> city_list = new LinkedList<>();

        public CityListRestResponse(List<CityDataSet> cityList){
            for (CityDataSet cityDataSet : cityList) {
                CityEntity city = new CityEntity(cityDataSet);
                this.city_list.add(city);
            }
        }

        public CityListRestResponse(CityDataSet city){
            this.city_list.add(new CityEntity(city));
        }
    }

    public int getReadAccessLevel(){
        return AccessLevel.MIN;
    }

    public int getWriteAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RestEntity> getEntityType(){
        return CityEntity.class;
    }

    public RestResponse getEntity(RestContext restContext){
        CityDataSet city = getCityDicService(restContext).getById(restContext.getId());
        if (city == null)
            return ErrorRestResponse.notFound();
        return new CityListRestResponse(city);
    }

    public RestResponse getEntityList(RestContext restContext){
        return new CityListRestResponse(getCityDicService(restContext).getAll());
    }

    public RestResponse createEntity(RestContext restContext){
        CityEntity entity = (CityEntity)restContext.getEntity();
        try {
            CityDataSet city = getCityDicService(restContext).create(entity.getName());
            return new CityListRestResponse(city);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
    }

    public RestResponse updateEntity(RestContext restContext){
        CityEntity entity = (CityEntity)restContext.getEntity();
        try {
            CityDataSet city = getCityDicService(restContext).update(restContext.getId(), entity.getName());
            return new CityListRestResponse(city);
        }
        catch (AlreadyExistException e){
            return ErrorRestResponse.alreadyExist();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    public RestResponse deleteEntity(RestContext restContext){
        try {
            getCityDicService(restContext).delete(restContext.getId());
            return new RestResponse();
        }
        catch (NotFoundException e){
            return ErrorRestResponse.notFound();
        }
    }

    private CityDictionaryService getCityDicService(RestContext restContext){
        return restContext.getApplication().getByType(CityDictionaryService.class);
    }
}
