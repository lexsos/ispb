package ispb.base.frontend.rest;

import ispb.base.Application;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.db.utils.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RestContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, String[]> params;
    private Application application;
    private DataSetFilter dataSetFilter;
    private DataSetSort dataSetSort;
    private Pagination pagination;
    private long id;
    private RestEntity entity;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public DataSetFilter getDataSetFilter() {
        return dataSetFilter;
    }

    public void setDataSetFilter(DataSetFilter dataSetFilter) {
        this.dataSetFilter = dataSetFilter;
    }

    public DataSetSort getDataSetSort() {
        return dataSetSort;
    }

    public void setDataSetSort(DataSetSort dataSetSort) {
        this.dataSetSort = dataSetSort;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEntity(RestEntity entity) {
        this.entity = entity;
    }

    public <T> T getEntityByType(Class<T> clazz){
        if (clazz.isInstance(entity))
            return (T)entity;
        return null;
    }
}
