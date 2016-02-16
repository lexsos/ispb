package ispb.base.frontend.rest;

import ispb.base.Application;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.sort.DataSetSortItem;
import ispb.base.db.field.SortDirection;

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
                                  Application application,
                                  DataSetFilter dataSetFilter,
                                  DataSetSort dataSetSort){
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

    public DataSetFilter getDataSetFilter(Map<String, String[]> params){
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

    protected RestSort getRestSort(Map<String, String[]> params){
        String[] sortData = params.get("sort");
        if (sortData == null || sortData.length < 1)
            return new RestSort();
        return new RestSort(sortData[0]);
    }

    public DataSetSort getDataSetSort(Map<String, String[]> params){
        RestSort restSort = getRestSort(params);
        DataSetSort dataSetSort = new DataSetSort();

        for (Iterator<RestSortItem> i=restSort.iterator();i.hasNext();){
            RestSortItem item = i.next();
            DataSetSortItem dataSetSortItem = restToDataSetSort(item);
            if (dataSetSortItem != null)
                dataSetSort.add(dataSetSortItem);
        }

        return dataSetSort;
    }

    protected DataSetSortItem restToDataSetSort(RestSortItem restSortItem){
        String fieldName = restSortItem.getProperty();
        SortDirection direction = restSortItem.isAsc()?SortDirection.ASC:SortDirection.DESC;
        return new DataSetSortItem(fieldName, direction);
    }
}
