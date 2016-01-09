package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcRequest;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutRpc extends RpcRequest {

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       Application application) throws ServletException, IOException {
        request.getSession().setAttribute("user", null);
        return true;
    }

    public int getAccessLevel(){
        return AccessLevel.ALL;
    }

    public boolean verify(){
        return true;
    }
}
