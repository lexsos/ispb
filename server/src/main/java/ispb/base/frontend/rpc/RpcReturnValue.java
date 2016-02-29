package ispb.base.frontend.rpc;


import ispb.base.frontend.utils.Jsonable;
import ispb.base.utils.GsonGetter;

public class RpcReturnValue implements Jsonable {

    public String toJson(){
        return GsonGetter.get().toJson(this);
    }
}
