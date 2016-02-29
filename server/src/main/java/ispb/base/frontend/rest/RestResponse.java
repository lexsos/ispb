package ispb.base.frontend.rest;

import ispb.base.frontend.utils.Jsonable;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.utils.GsonGetter;

public class RestResponse implements Jsonable {

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
        return GsonGetter.get().toJson(this);
    }
}
