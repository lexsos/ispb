package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.UserAccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginExistRpc extends RpcProcedure {

    private static class LoginExistArgs extends RpcArg {
        private String login;

        public boolean verify() {
            return getLogin() != null;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.ADMIN;
    }

    public Class<? extends RpcArg> getArgType(){
        return LoginExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        LoginExistArgs arg = (LoginExistArgs) obj;
        return application.getByType(UserAccountService.class).loginExist(arg.getLogin());
    }
}
