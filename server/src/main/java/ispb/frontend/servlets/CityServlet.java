package ispb.frontend.servlets;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.response.CityListRestResponse;
import ispb.base.frontend.response.CityRestResponse;
import ispb.base.frontend.utils.RestResponse;
import ispb.frontend.utils.ApiBaseServlet;

import java.util.Map;

public class CityServlet extends ApiBaseServlet {

    protected RestResponse getEntity(long id, Map<String, String[]> params){
        CityDataSet city = getApplication().getDaoFactory().getCityDao().getById(id);
        if (city == null)
            return null;
        return new CityRestResponse(city);
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
}
