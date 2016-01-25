package ispb.base.frontend.rest;

import ispb.base.Application;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

public abstract class RestResource implements RestAccessLevelable {

    public abstract Class<RestEntity> getEntityType();

    public RestResponse getEntity(HttpServletRequest request,
                                  HttpServletResponse response,
                                  long id,
                                  Map<String, String[]> params,
                                  Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse getEntityList(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Map<String, String[]> params,
                                  Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse createEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestEntity entity,
                                     Map<String, String[]> params,
                                     Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse updateEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     RestEntity entity,
                                     Map<String, String[]> params,
                                     Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse deleteEntityList(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Map<String, String[]> params,
                                         Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse deleteEntity(HttpServletRequest request,
                                     HttpServletResponse response,
                                     long id,
                                     Map<String, String[]> params,
                                     Application application){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestFilter getRestFilter(Map<String, String[]> params){
        String[] filtersData = params.get("filter");
        if (filtersData == null || filtersData.length < 1)
            return new  RestFilter();
        return new RestFilter(filtersData[0]);
    }

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        return null;
    }

    protected DataSetFilter getDataSetFilter(Map<String, String[]> params){
        RestFilter restFilter = getRestFilter(params);
        DataSetFilter dataSetFilter = new DataSetFilter();

        for (Iterator<RestFilterItem> i = restFilter.iterator(); i.hasNext();){
            RestFilterItem item = i.next();
            DataSetFilterItem dataSetFilterItem = restToDataSetFilter(item);
            if (dataSetFilterItem != null)
                dataSetFilter.add(dataSetFilterItem);
        }

        return dataSetFilter;
    }
}
