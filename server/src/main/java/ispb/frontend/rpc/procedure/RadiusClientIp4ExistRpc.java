package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.Ip4AddressArgs;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.RadiusClientDictionaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RadiusClientIp4ExistRpc extends RpcProcedure {

    public int getAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RpcArg> getArgType() {
        return Ip4AddressArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        Ip4AddressArgs arg = (Ip4AddressArgs) obj;
        return application.getByType(RadiusClientDictionaryService.class).ip4exist(arg.getIp4Address());
    }
}
