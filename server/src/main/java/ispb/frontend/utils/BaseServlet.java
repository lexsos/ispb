package ispb.frontend.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import ispb.ApplicationImpl;
import ispb.base.Application;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.exception.IncompatibleDataStruct;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.utils.Verifiable;


public class BaseServlet extends HttpServlet  {

    private static final Gson GSON = new Gson();

    protected Application getApplication(){
        return ApplicationImpl.getApplication();
    }

    protected void writeJson(HttpServletResponse response, String json) throws IOException{

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(json);
    }

    protected <T> T readJsonObject(HttpServletRequest request, Class<T> clazz){
        T jsonObj = null;
        try {
            jsonObj = GSON.fromJson(request.getReader(), clazz);
        }
        catch (JsonParseException e){
            // TODO: Log error
        }
        catch (IOException e){
            // TODO: Log error
        }
        return jsonObj;
    }

    protected <T> T prepareJsonRequest(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Class<T> clazz)  throws ReadJsonError, IncompatibleDataStruct {
        T obj = this.readJsonObject(request, clazz);
        if (obj == null)
            throw new ReadJsonError();
        if (obj instanceof Verifiable)
            if (!((Verifiable)obj).verify())
                throw new IncompatibleDataStruct();
        return obj;
    }

    protected UserDataSet getCurrentUser(HttpServletRequest request){
        UserDataSet user = (UserDataSet)request.getSession().getAttribute("user");
        return user;
    }
}
