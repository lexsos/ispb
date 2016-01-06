package ispb.frontend.utils;

import ispb.base.frontend.response.ErrorRestResponse;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.frontend.utils.RestEntity;
import ispb.base.frontend.utils.RestResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public abstract class ApiBaseServlet<TEntity extends RestEntity> extends BaseServlet {

    protected void writeFailMessage(HttpServletResponse response, String msg, int code) throws IOException {
        String json = new ErrorRestResponse(msg, code).toJson();
        this.writeJson(response, json);
    }

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
            TEntity entity = (TEntity)prepareJsonRequest(request, response, getEntityType());
            if (entity == null)
                return;
            restResponse = createEntity(entity, parameterMap, request, response);
        }
        else if(Objects.equals(method, "PUT") && id != null){
            TEntity entity = (TEntity)prepareJsonRequest(request, response, getEntityType());
            if (entity == null)
                return;
            restResponse = updateEntity(id, entity, parameterMap, request, response);
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
