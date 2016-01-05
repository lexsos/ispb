package ispb.base.frontend.utils;

import com.google.gson.Gson;
import ispb.base.frontend.utils.Jsonable;

public class RestResponse implements Jsonable {

    private static final Gson GSON = new Gson();
    private boolean success = true;
    private int code = ResponseCodes.OK;

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
