package ispb.base.frontend.rest;

import ispb.base.frontend.utils.RestBase;

public class RestMessage extends RestBase {
    private String message;
    private int code;
    boolean success;

    public RestMessage(){
        message = null;
        code = -1;
        success = true;
    }

    public RestMessage(String message, int code){
        this.message = message;
        this.code = code;
        success = true;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean getSuccess(){
        return success;
    }
}
