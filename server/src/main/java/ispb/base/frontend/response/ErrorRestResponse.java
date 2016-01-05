package ispb.base.frontend.response;

import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.frontend.utils.RestResponse;

import java.util.LinkedList;
import java.util.List;

public class ErrorRestResponse extends RestResponse {
    private List<String> errors;

    public ErrorRestResponse(){
        setErrors(new LinkedList<String>());
        setCode(ResponseCodes.BAD_REQUEST);
        setSuccess(false);
    }

    public ErrorRestResponse(String msg, int code){
        setErrors(new LinkedList<String>());
        getErrors().add(msg);
        setCode(code);
        setSuccess(false);
    }

    public ErrorRestResponse(String[] msgList, int code){
        setErrors(new LinkedList<String>());
        for (int i=0; i<msgList.length; i++)
            getErrors().add(msgList[i]);
        setCode(code);
        setSuccess(false);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static ErrorRestResponse alreadyExist(){
        return new ErrorRestResponse("Entity already exist.", ResponseCodes.ENTITY_ALREADY_EXIST);
    }
}
