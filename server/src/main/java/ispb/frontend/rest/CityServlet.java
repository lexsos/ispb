package ispb.frontend.rest;

import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.rest.entity.CityRestEntity;
import ispb.base.frontend.rest.response.CityListRestResponse;
import ispb.base.frontend.rest.response.ErrorRestResponse;
import ispb.base.frontend.rest.utils.RestResponse;
import ispb.frontend.utils.RestBaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CityServlet extends RestBaseServlet<CityRestEntity> {

    protected RestResponse getEntity(long id,
                                     Map<String, String[]> params,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        CityDataSet city = getApplication().getDaoFactory().getCityDao().getById(id);
        if (city == null)
            return null;
        return new CityListRestResponse(city);
    }

    protected RestResponse getEntityList(Map<String, String[]> params,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return new CityListRestResponse(getApplication().getDaoFactory().getCityDao().getAll());
    }

    protected RestResponse delEntity(long id, Map<String, String[]> params,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        // TODO: delete entity
        return null;
    }

    protected RestResponse delEntityList(Map<String, String[]> params,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        // TODO: delete entity list
        return null;
    }

    protected RestResponse createEntity(CityRestEntity entity,
                                        Map<String, String[]> params,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        CityDataSetDao dao = getApplication().getDaoFactory().getCityDao();
        if (dao.getByName(entity.getName()) != null)
            return ErrorRestResponse.alreadyExist();
        CityDataSet city = new CityDataSet();
        city.setName(entity.getName());
        dao.save(city);
        return new CityListRestResponse(city);
    }

    protected RestResponse updateEntity(long id,
                                        CityRestEntity entity,
                                        Map<String, String[]> params,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
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
