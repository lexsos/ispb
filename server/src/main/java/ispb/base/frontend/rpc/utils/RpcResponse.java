package ispb.base.frontend.rpc.utils;

import com.google.gson.Gson;
import ispb.base.frontend.utils.Jsonable;
import ispb.base.frontend.utils.ResponseCodes;

public class RpcResponse implements Jsonable {

    private static final Gson GSON = new Gson();
    private int code = ResponseCodes.OK;
    private Object value = null;

    public RpcResponse(){}

    public RpcResponse(Object value){
        setValue(value);
    }

    public RpcResponse(Object value, int code){
        setValue(value);
        setCode(code);
    }

    public String toJson(){
        return GSON.toJson(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static RpcResponse jsonError(){
        return new RpcResponse(null, ResponseCodes.JSON_ERROR);
    }

    public static RpcResponse incompatibleDataStruct(){
        return new RpcResponse(null, ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
    }

    public static RpcResponse internalError(){
        return new RpcResponse(null, ResponseCodes.INTERNAL_ERROR);
    }

    public static RpcResponse notFound(){
        return new RpcResponse(null, ResponseCodes.NOT_FOUND);
    }
}
