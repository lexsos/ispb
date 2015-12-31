package ispb.frontend.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import ispb.ApplicationImpl;
import ispb.base.Application;
import ispb.base.frontend.response.MessageRestResponse;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.frontend.utils.Verifiable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    protected void writeMessage(HttpServletResponse response, String msg, int code, boolean success) throws IOException{
        MessageRestResponse message = new MessageRestResponse(msg, code, success);
        this.writeJson(response, message.toJson());
    }

    protected void writeFailMessage(HttpServletResponse response, String msg, int code) throws IOException{
        writeMessage(response, msg, code, false);
    }

    protected void writeSuccessMessage(HttpServletResponse response, String msg) throws IOException{
        writeMessage(response, msg, ResponseCodes.OK, true);
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
                                       Class<T> clazz)  throws IOException{
        T obj = this.readJsonObject(request, clazz);
        if (obj == null) {
            this.writeFailMessage(response, "Error in JSON structure.", ResponseCodes.JSON_ERROR);
            return null;
        }
        if (obj instanceof Verifiable)
            if (!((Verifiable)obj).verify()){
                this.writeFailMessage(response, "Incompatible data structure format.", ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
                return null;
            }
        return obj;
    }
}
