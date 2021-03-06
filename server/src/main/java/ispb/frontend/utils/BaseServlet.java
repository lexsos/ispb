package ispb.frontend.utils;


import com.google.gson.JsonParseException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ispb.ApplicationImpl;
import ispb.base.Application;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.exception.IncompatibleDataStructure;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.utils.Verifiable;
import ispb.base.service.LogService;
import ispb.base.utils.GsonGetter;


public class BaseServlet extends HttpServlet  {

    private Properties typeList;

    protected Application getApplication(){
        return ApplicationImpl.getApplication();
    }

    protected LogService getLogService(){
        return getApplication().getByType(LogService.class);
    }

    protected void writeJson(HttpServletResponse response, String json) throws IOException{

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(json);
    }

    private <T> T readJsonObject(HttpServletRequest request, Class<T> clazz){
        T jsonObj = null;
        try {
            jsonObj = GsonGetter.get().fromJson(request.getReader(), clazz);
        }
        catch (JsonParseException e){
            getLogService().debug("Can't parse json", e);
        }
        catch (IOException e){
            getLogService().info("Can't read request", e);
        }
        return jsonObj;
    }

    protected <T> T prepareJsonRequest(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Class<T> clazz)  throws ReadJsonError, IncompatibleDataStructure {
        T obj = this.readJsonObject(request, clazz);
        if (obj == null)
            throw new ReadJsonError();
        if (obj instanceof Verifiable)
            if (!((Verifiable)obj).verify())
                throw new IncompatibleDataStructure();
        return obj;
    }

    protected UserDataSet getCurrentUser(HttpServletRequest request){
        return  (UserDataSet)request.getSession().getAttribute("user");
    }

    protected void loadTypes(String resourceName){
        typeList = new Properties();
        InputStream in = getClass().getResourceAsStream(resourceName);
        try {
            typeList.load(in);
        }
        catch (IOException e){
            getLogService().warn("Can't load resource: " + resourceName, e);
        }
    }

    protected Class getTypeByKey(String key){
        String typeName = typeList.getProperty(key, null);
        if (typeName == null)
            return null;

        Class clazz;
        try {
            clazz = Class.forName(typeName);
        }
        catch (ClassNotFoundException e){
            getLogService().warn("Type not found: " + typeName, e);
            return null;
        }

        return clazz;
    }
}
