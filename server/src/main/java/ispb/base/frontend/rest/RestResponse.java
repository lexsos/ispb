package ispb.base.frontend.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ispb.base.frontend.utils.Jsonable;
import ispb.base.frontend.utils.ResponseCodes;

public class RestResponse implements Jsonable {

    private static final Gson GSON;
    private boolean success = true;
    private int code = ResponseCodes.OK;

    static {
        GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toJson(){
        return GSON.toJson(this);
    }
}
