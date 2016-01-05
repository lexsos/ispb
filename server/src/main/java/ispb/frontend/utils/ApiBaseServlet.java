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
                    this.writeFailMessage(response, "The id must be integer.", ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
                    return;
                }
        }

        String method = request.getMethod();
        RestResponse restResponse;
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (Objects.equals(method, "GET") && id == null)
            restResponse = getEntityList(parameterMap);
        else if (Objects.equals(method, "GET") && id != null)
            restResponse = getEntity(id, parameterMap);
        else if (Objects.equals(method, "DELETE") && id == null)
            restResponse = delEntity(id, parameterMap);
        else if (Objects.equals(method, "DELETE") && id != null)
            restResponse = delEntityList(parameterMap);
        else if(Objects.equals(method, "POST") && id == null){
            TEntity entity = (TEntity)prepareJsonRequest(request, response, getEntityType());
            if (entity == null)
                return;
            restResponse = createEntity(entity, parameterMap);
        }
        else if(Objects.equals(method, "PUT") && id != null){
            TEntity entity = (TEntity)prepareJsonRequest(request, response, getEntityType());
            if (entity == null)
                return;
            restResponse = updateEntity(id, entity, parameterMap);
        }
        else {
            this.writeFailMessage(response, "Method not implemented.", ResponseCodes.METHOD_NOT_ALLOWED);
            return;
        }

        if (restResponse == null){
            this.writeFailMessage(response, "Not found.", ResponseCodes.NOT_FOUND);
            return;
        }

        writeJson(response, restResponse.toJson());
    }

    protected abstract RestResponse getEntity(long id, Map<String, String[]> params);
    protected abstract RestResponse getEntityList(Map<String, String[]> params);
    protected abstract RestResponse delEntity(long id, Map<String, String[]> params);
    protected abstract RestResponse delEntityList(Map<String, String[]> params);
    protected abstract RestResponse createEntity(TEntity entity, Map<String, String[]> params);
    protected abstract RestResponse updateEntity(long id, TEntity entity, Map<String, String[]> params);

    protected abstract Class getEntityType();
}
