package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.RadiusUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RadiusLoginExistRpc  extends RpcProcedure {

    private static class RadiusLoginArgs extends RpcArg {

        private String login;

        public boolean verify() {
            return getLogin() != null;
        }

        public String getLogin() {
            return login;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class<? extends RpcArg> getArgType(){
        return RadiusLoginArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        RadiusLoginArgs args = (RadiusLoginArgs) obj;
        RadiusUserService service = application.getByType(RadiusUserService.class);
        return service.userNameExist(args.getLogin());
    }

}
