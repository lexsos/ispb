package ispb.frontend.rpc;

import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.exception.IncompatibleDataStruct;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcRequest;
import ispb.base.frontend.rpc.RpcResponse;
import ispb.base.frontend.utils.AccessLevel;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RpcServlet extends BaseServlet {

    public RpcServlet(){
        super();
        loadTypes("rpc_procedure.properties");
    }

    private Class getRpcRequestClass(String procedureName){
        return getTypeByKey(procedureName);
    }

    protected void writeRpcResponse(HttpServletRequest request,
                                    HttpServletResponse response,
                                    RpcResponse rpcResponse) throws IOException {
        writeJson(response, rpcResponse.toJson());
    }

    protected void doPost( HttpServletRequest request,
                            HttpServletResponse response ) throws ServletException, IOException {
        String[] path_param = request.getPathInfo().split("/");
        if (path_param.length != 2){
            writeRpcResponse(request, response, RpcResponse.notFound());
            return;
        }

        String procedureName = path_param[1];
        Class rpcRequestClass = getRpcRequestClass(procedureName);
        if (rpcRequestClass == null){
            writeRpcResponse(request, response, RpcResponse.notFound());
            return;
        }

        RpcRequest req;
        try {
            req = (RpcRequest)rpcRequestClass.newInstance();
        }
        catch (Throwable e){
            // TODO: log error
            writeRpcResponse(request, response, RpcResponse.internalError());
            return;
        }

        RpcArg arg;
        try {
            arg = prepareJsonRequest(request, response, req.getArgType());
        }
        catch (ReadJsonError e){
            writeRpcResponse(request, response, RpcResponse.jsonError());
            return;
        }
        catch (IncompatibleDataStruct e){
            writeRpcResponse(request, response, RpcResponse.incompatibleDataStruct());
            return;
        }

        if (req.getAccessLevel() != AccessLevel.ALL){
            UserDataSet user = getCurrentUser(request);
            if (user == null){
                writeRpcResponse(request, response, RpcResponse.unauthorized());
                return;
            }
            if (user.getAccessLevel() < req.getAccessLevel()){
                writeRpcResponse(request, response, RpcResponse.lowAccessLevel());
                return;
            }
        }

        Object value;
        try {
            value = req.call(request, response, arg, getApplication());
        }
        catch (Throwable e){
            // TODO: log error
            writeRpcResponse(request, response, RpcResponse.internalError());
            return;
        }

        writeRpcResponse(request, response, new RpcResponse(value));
    }
}
