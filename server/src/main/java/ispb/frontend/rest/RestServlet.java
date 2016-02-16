package ispb.frontend.rest;

import ispb.base.db.dataset.UserDataSet;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.sort.DataSetSort;
import ispb.base.frontend.exception.IncompatibleDataStruct;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.rest.ErrorRestResponse;
import ispb.base.frontend.rest.RestEntity;
import ispb.base.frontend.rest.RestResource;
import ispb.base.frontend.rest.RestResponse;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.LogService;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class RestServlet extends BaseServlet {

    public RestServlet(){
        super();
        loadTypes("rest_resource.properties");
    }

    private Class getRestResourceClass(String resourceName){
        return getTypeByKey(resourceName);
    }

    protected void writeRestResponse(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestResponse restResponse) throws IOException{
        writeJson(response, restResponse.toJson());
    }

    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        String[] pathParam = request.getPathInfo().split("/");
        if (pathParam.length != 2 && pathParam.length != 3){
            writeRestResponse(request, response, ErrorRestResponse.notFound());
            return;
        }

        String resourceName = pathParam[1];
        Class restResourceClass = getRestResourceClass(resourceName);
        if (restResourceClass == null){
            writeRestResponse(request, response, ErrorRestResponse.notFound());
            return;
        }

        Long id = null;
        if (pathParam.length == 3)
            try {
                id = Long.parseLong(pathParam[2]);
            }
            catch (Exception e){
                writeRestResponse(request, response, ErrorRestResponse.IntegerId());
                return;
            }

        RestResource resource;
        try {
            resource = (RestResource)restResourceClass.newInstance();
        }
        catch (Exception e){
            getLogService().error("Can't create instance for REST resource", e);
            writeRestResponse(request, response, ErrorRestResponse.internalError());
            return;
        }

        int requiredAccessLevel = resource.getWriteAccessLevel();
        String method = request.getMethod();
        if (Objects.equals(method, "GET"))
            requiredAccessLevel = resource.getReadAccessLevel();

        if(requiredAccessLevel != AccessLevel.ALL){
            UserDataSet user = getCurrentUser(request);
            if (user == null){
                writeRestResponse(request, response, ErrorRestResponse.unauthorized());
                return;
            }
            if (user.getAccessLevel() < requiredAccessLevel){
                writeRestResponse(request, response, ErrorRestResponse.lowAccessLevel());
                return;
            }
        }

        Map<String, String[]> parameterMap = request.getParameterMap();

        DataSetFilter dataSetFilter;
        try {
            dataSetFilter = resource.getDataSetFilter(parameterMap);
        }
        catch (Throwable e){
            writeRestResponse(request, response, ErrorRestResponse.restFilterError());
            return;
        }

        DataSetSort dataSetSort;
        try {
            dataSetSort = resource.getDataSetSort(parameterMap);
        }
        catch (Throwable e){
            writeRestResponse(request, response, ErrorRestResponse.restSortError());
            return;
        }

        RestResponse restResponse = ErrorRestResponse.methodNotAllowed();
        try {
            if (Objects.equals(method, "GET") && id == null)
                restResponse = resource.getEntityList(request, response, parameterMap, getApplication(), dataSetFilter, dataSetSort);
            else if (Objects.equals(method, "GET") && id != null)
                restResponse = resource.getEntity(request, response, id, parameterMap, getApplication());
            else if (Objects.equals(method, "DELETE") && id == null)
                restResponse = resource.deleteEntityList(request, response, parameterMap, getApplication());
            else if (Objects.equals(method, "DELETE") && id != null)
                restResponse = resource.deleteEntity(request, response, id, parameterMap, getApplication());
            else if (Objects.equals(method, "POST") && id == null)
                try {
                    RestEntity entity = prepareJsonRequest(request, response, resource.getEntityType());
                    restResponse = resource.createEntity(request, response, entity, parameterMap, getApplication());
                } catch (ReadJsonError e) {
                    restResponse = ErrorRestResponse.jsonError();
                } catch (IncompatibleDataStruct e) {
                    restResponse = ErrorRestResponse.incompatibleDataStruct();
                }
            else if (Objects.equals(method, "PUT") && id != null)
                try {
                    RestEntity entity = prepareJsonRequest(request, response, resource.getEntityType());
                    restResponse = resource.updateEntity(request, response, id, entity, parameterMap, getApplication());
                } catch (ReadJsonError e) {
                    restResponse = ErrorRestResponse.jsonError();
                } catch (IncompatibleDataStruct e) {
                    restResponse = ErrorRestResponse.incompatibleDataStruct();
                }
        }
        catch (Throwable e){
            getLogService().error("Can't execute REST resource handler", e);
            restResponse = ErrorRestResponse.internalError();
        }

        if (restResponse == null)
            restResponse = ErrorRestResponse.notFound();

        writeRestResponse(request, response, restResponse);
    }
}
