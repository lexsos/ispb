package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.rpc.VoidArg;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.RadiusUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RadiusClearAuthRequest extends RpcProcedure {

    public int getAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RpcArg> getArgType() {
        return VoidArg.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        getRadiusUserService(application).clearAuthRequest();
        return true;
    }

    private RadiusUserService getRadiusUserService(Application application){
        return application.getByType(RadiusUserService.class);
    }
}
