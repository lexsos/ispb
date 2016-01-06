package ispb.base.frontend.response;

import ispb.base.frontend.rest.utils.RestResponse;

public class MessageRestResponse extends RestResponse {
    private String message = null;

    public MessageRestResponse(String message, int code, boolean success){
        this.message = message;
        setCode(code);
        setSuccess(success);
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
