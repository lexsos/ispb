package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRpc extends RpcProcedure {

    private static class LoginArgs extends RpcArg {

        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean verify(){
            if (login != null && password != null)
                return true;
            return false;
        }
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {

        LoginArgs arg = (LoginArgs)obj;
        UserDataSet user = application.getUserAccountService().auth(arg.getLogin(), arg.getPassword());
        if (user == null)
            return false;
        request.getSession().setAttribute("user", user);
        return true;
    }

    public int getAccessLevel(){
        return AccessLevel.ALL;
    }

    public Class getArgType(){
        return LoginArgs.class;
    }
}
