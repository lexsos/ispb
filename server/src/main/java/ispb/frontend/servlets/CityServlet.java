package ispb.frontend.servlets;

import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.entity.CityRestEntity;
import ispb.base.frontend.response.CityListRestResponse;
import ispb.base.frontend.response.ErrorRestResponse;
import ispb.base.frontend.utils.RestResponse;
import ispb.frontend.utils.ApiBaseServlet;

import java.util.Map;

public class CityServlet extends ApiBaseServlet<CityRestEntity> {

    protected RestResponse getEntity(long id, Map<String, String[]> params){
        CityDataSet city = getApplication().getDaoFactory().getCityDao().getById(id);
        if (city == null)
            return null;
        return new CityListRestResponse(city);
    }

    protected RestResponse getEntityList(Map<String, String[]> params){
        return new CityListRestResponse(getApplication().getDaoFactory().getCityDao().getAll());
    }

    protected RestResponse delEntity(long id, Map<String, String[]> params){
        return null;
    }

    protected RestResponse delEntityList(Map<String, String[]> params){
        return null;
    }

    protected RestResponse createEntity(CityRestEntity entity, Map<String, String[]> params){
        CityDataSetDao dao = getApplication().getDaoFactory().getCityDao();
        if (dao.getByName(entity.getName()) != null)
            return ErrorRestResponse.alreadyExist();
        CityDataSet city = new CityDataSet();
        city.setName(entity.getName());
        dao.save(city);
        return new CityListRestResponse(city);
    }

    protected RestResponse updateEntity(long id, CityRestEntity entity, Map<String, String[]> params){
        CityDataSetDao dao = getApplication().getDaoFactory().getCityDao();
        CityDataSet city = dao.getById(id);
        if (city == null)
            return null; // TODO: Return error
        city.setName(entity.getName()); // TODO: Check not city exist
        dao.save(city);
        return new CityListRestResponse(city);
    }

    protected Class getEntityType(){
        return CityRestEntity.class;
    }
}
