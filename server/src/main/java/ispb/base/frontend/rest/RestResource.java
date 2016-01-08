package ispb.base.frontend.rest;

import ispb.base.Application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract class RestResource {

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
}
