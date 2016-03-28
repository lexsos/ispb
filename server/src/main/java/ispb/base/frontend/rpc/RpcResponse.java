package ispb.base.frontend.rpc;


import ispb.base.frontend.utils.Jsonable;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.utils.GsonGetter;

public class RpcResponse implements Jsonable {

    private int code = ResponseCodes.OK;
    private Object value;

    public RpcResponse(){}

    public RpcResponse(Object value){
        setValue(value);
    }

    public RpcResponse(Object value, int code){
        setValue(value);
        setCode(code);
    }

    public String toJson(){
        return GsonGetter.get().toJson(this);
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

    public static RpcResponse incompatibleDataStructure(){
        return new RpcResponse(null, ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
    }

    public static RpcResponse internalError(){
        return new RpcResponse(null, ResponseCodes.INTERNAL_ERROR);
    }

    public static RpcResponse notFound(){
        return new RpcResponse(null, ResponseCodes.NOT_FOUND);
    }

    public static RpcResponse lowAccessLevel(){
        return new RpcResponse(null, ResponseCodes.LOW_CURRENT_ACCESS_LEVEL);
    }

    public static RpcResponse unauthorized(){
        return new RpcResponse(null, ResponseCodes.UNAUTHORIZED);
    }
}
