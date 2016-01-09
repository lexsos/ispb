package ispb.base.frontend.rpc;

import ispb.base.Application;
import ispb.base.frontend.utils.Verifiable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RpcRequest implements Verifiable, RpcAccessLevelable {

    public abstract Object call(HttpServletRequest request,
                                HttpServletResponse response,
                                Application application) throws ServletException, IOException;
}
