package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.utils.Ip4Address;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Ip4AddressValidateRpc extends RpcProcedure {

    private static class Ip4AddressArgs extends RpcArg {

        private String ip4Address;

        public boolean verify() {
            return getIp4Address() != null;
        }

        public String getIp4Address() {
            return ip4Address;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class<? extends RpcArg> getArgType(){
        return Ip4AddressArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        Ip4AddressArgs args = (Ip4AddressArgs) obj;
        return Ip4Address.normalize(args.getIp4Address()) != null;
    }
}
