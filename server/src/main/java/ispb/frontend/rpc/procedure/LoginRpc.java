package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.rpc.RpcRequest;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRpc extends RpcRequest {

    private String login;
    private String password;

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       Application application) throws ServletException, IOException {

        UserDataSet user = application.getUserAccountService().auth(getLogin(), getPassword());
        if (user == null)
            return false;
        request.getSession().setAttribute("user", user);
        return true;
    }

    public boolean verify(){
        if (login != null && password != null)
            return true;
        return false;
    }

    public int getAccessLevel(){
        return AccessLevel.ALL;
    }

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
}
