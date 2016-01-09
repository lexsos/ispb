package ispb.base.frontend.rest;

import ispb.base.frontend.utils.ResponseCodes;

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

    public static ErrorRestResponse methodNotAllowed(){
        return new ErrorRestResponse("Method not allowed.", ResponseCodes.METHOD_NOT_ALLOWED);
    }

    public static ErrorRestResponse notFound(){
        return new ErrorRestResponse("Not found.", ResponseCodes.NOT_FOUND);
    }

    public static ErrorRestResponse IntegerId(){
        return new ErrorRestResponse("The id must be integer.", ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
    }

    public static ErrorRestResponse jsonError(){
        return new ErrorRestResponse("Error in JSON structure.", ResponseCodes.JSON_ERROR);
    }

    public static ErrorRestResponse incompatibleDataStruct(){
        return new ErrorRestResponse("Incompatible data structure format.", ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
    }

    public static ErrorRestResponse internalError(){
        return new ErrorRestResponse("Internal server error.", ResponseCodes.INTERNAL_ERROR);
    }

    public static ErrorRestResponse unauthorized(){
        return new ErrorRestResponse("Unauthorized.", ResponseCodes.UNAUTHORIZED);
    }

    public static ErrorRestResponse lowAccessLevel(){
        return new ErrorRestResponse("Low current access level.", ResponseCodes.LOW_CURRENT_ACCESS_LEVEL);
    }
}
