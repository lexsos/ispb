package ispb.base.frontend.rpc.utils;

import ispb.base.Application;
import ispb.base.frontend.utils.Verifiable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RpcRequest implements Verifiable {

    public abstract Object Do(HttpServletRequest request,
                              HttpServletResponse response,
                              Application application) throws ServletException, IOException;
}
