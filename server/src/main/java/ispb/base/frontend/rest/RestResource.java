package ispb.base.frontend.rest;

import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.filter.DataSetFilterItem;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.sort.DataSetSortItem;
import ispb.base.db.field.SortDirection;

import java.util.Map;

@SuppressWarnings("UnusedParameters")
public abstract class RestResource implements RestAccessLevelable {

    public abstract Class<? extends RestEntity> getEntityType();

    public RestResponse getEntity(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse getEntityList(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse createEntity(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse updateEntity(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse deleteEntityList(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public RestResponse deleteEntity(RestContext restContext){
        return ErrorRestResponse.methodNotAllowed();
    }

    public DataSetFilter getDataSetFilter(Map<String, String[]> params){
        RestFilter restFilter = getRestFilter(params);
        DataSetFilter dataSetFilter = new DataSetFilter();

        for (RestFilterItem item : restFilter) {
            DataSetFilterItem dataSetFilterItem = restToDataSetFilter(item);
            if (dataSetFilterItem != null)
                dataSetFilter.add(dataSetFilterItem);
        }

        return dataSetFilter;
    }

    public DataSetSort getDataSetSort(Map<String, String[]> params){
        RestSort restSort = getRestSort(params);
        DataSetSort dataSetSort = new DataSetSort();

        for (RestSortItem item : restSort) {
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

    protected DataSetFilterItem restToDataSetFilter(RestFilterItem restItem){
        return null;
    }

    private RestSort getRestSort(Map<String, String[]> params){
        String[] sortData = params.get("sort");
        if (sortData == null || sortData.length < 1)
            return new RestSort();
        return new RestSort(sortData[0]);
    }

    private RestFilter getRestFilter(Map<String, String[]> params){
        String[] filtersData = params.get("filter");
        if (filtersData == null || filtersData.length < 1)
            return new  RestFilter();
        return new RestFilter(filtersData[0]);
    }
}
