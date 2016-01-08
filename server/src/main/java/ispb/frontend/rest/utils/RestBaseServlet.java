package ispb.frontend.rest.utils;

import ispb.base.frontend.exception.IncompatibleDataStruct;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.rest.response.ErrorRestResponse;
import ispb.base.frontend.rest.utils.RestEntity;
import ispb.base.frontend.rest.utils.RestResponse;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public abstract class RestBaseServlet<TEntity extends RestEntity> extends BaseServlet {

    protected void writeRestResponse(HttpServletRequest request,
                                     HttpServletResponse response,
                                     RestResponse restResponse) throws IOException {
        writeJson(response, restResponse.toJson());
    }

    protected void service( HttpServletRequest request,
                            HttpServletResponse response ) throws ServletException, IOException {

        Long id = null;
        String path = request.getPathInfo();
        if (path != null) {
            String[] params = request.getPathInfo().split("/");
            if (params.length > 1)
                try {
                    id = Long.parseLong(params[1]);
                }
                catch (Exception e){
                    writeRestResponse(request, response, ErrorRestResponse.IntegerId());
                    return;
                }
        }

        String method = request.getMethod();
        RestResponse restResponse;
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (Objects.equals(method, "GET") && id == null)
            restResponse = getEntityList(parameterMap, request, response);
        else if (Objects.equals(method, "GET") && id != null)
            restResponse = getEntity(id, parameterMap, request, response);
        else if (Objects.equals(method, "DELETE") && id == null)
            restResponse = delEntity(id, parameterMap, request, response);
        else if (Objects.equals(method, "DELETE") && id != null)
            restResponse = delEntityList(parameterMap, request, response);
        else if(Objects.equals(method, "POST") && id == null){
            try {
                TEntity entity = (TEntity) prepareJsonRequest(request, response, getEntityType());
                restResponse = createEntity(entity, parameterMap, request, response);
            }
            catch (ReadJsonError e){
                restResponse = ErrorRestResponse.jsonError();
            }
            catch (IncompatibleDataStruct e){
                restResponse = ErrorRestResponse.incompatibleDataStruct();
            }
        }
        else if(Objects.equals(method, "PUT") && id != null){
            try {
                TEntity entity = (TEntity)prepareJsonRequest(request, response, getEntityType());
                restResponse = updateEntity(id, entity, parameterMap, request, response);
            }
            catch (ReadJsonError e){
                restResponse = ErrorRestResponse.jsonError();
            }
            catch (IncompatibleDataStruct e){
                restResponse = ErrorRestResponse.incompatibleDataStruct();
            }
        }
        else
            restResponse = ErrorRestResponse.methodNotAllowed();

        if (restResponse == null)
            restResponse = ErrorRestResponse.notFound();

        writeRestResponse(request, response, restResponse);
    }

    protected RestResponse getEntity(long id,
                                     Map<String, String[]> params,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestResponse getEntityList(Map<String, String[]> params,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestResponse delEntity(long id,
                                     Map<String, String[]> params,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestResponse delEntityList(Map<String, String[]> params,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestResponse createEntity(TEntity entity,
                                        Map<String, String[]> params,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected RestResponse updateEntity(long id,
                                        TEntity entity,
                                        Map<String, String[]> params,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return ErrorRestResponse.methodNotAllowed();
    }

    protected abstract Class getEntityType();
}
