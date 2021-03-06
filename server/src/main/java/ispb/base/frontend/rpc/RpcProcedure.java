package ispb.base.frontend.rpc;

import ispb.base.Application;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RpcProcedure implements RpcAccessLevelable {

    public abstract Object call(HttpServletRequest request,
                                HttpServletResponse response,
                                RpcArg obj,
                                Application application) throws ServletException, IOException;

    public Class<? extends RpcArg> getArgType(){
        return VoidArg.class;
    }
}
