package ispb.frontend.rpc;

import ispb.base.frontend.exception.IncompatibleDataStruct;
import ispb.base.frontend.exception.ReadJsonError;
import ispb.base.frontend.rpc.utils.RpcRequest;
import ispb.base.frontend.rpc.utils.RpcResponse;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RpcServlet extends BaseServlet {

    private Properties requestTypes;

    public RpcServlet(){
        super();
        loadTypes();
    }

    protected void loadTypes(){
        requestTypes = new Properties();
        InputStream in = getClass().getResourceAsStream("rpc_procedure.properties");
        try {
            requestTypes.load(in);
        }
        catch (IOException e){
            // TODO: log error msg
        }
    }

    private Class getRpcRequestClass(String procedureName){
        String typeName = requestTypes.getProperty(procedureName, null);
        if (typeName == null)
            return null;

        Class clazz;
        try {
            clazz = Class.forName(typeName);
        }
        catch (ClassNotFoundException e){
            // TODO: log error
            return null;
        }

        return clazz;
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
            req = (RpcRequest)prepareJsonRequest(request, response, rpcRequestClass);
        }
        catch (ReadJsonError e){
            writeRpcResponse(request, response, RpcResponse.jsonError());
            return;
        }
        catch (IncompatibleDataStruct e){
            writeRpcResponse(request, response, RpcResponse.incompatibleDataStruct());
            return;
        }

        Object value;
        try {
            value = req.Do(request, response, getApplication());
        }
        catch (Throwable e){
            // TODO: log error
            writeRpcResponse(request, response, RpcResponse.internalError());
            return;
        }

        writeRpcResponse(request, response, new RpcResponse(value));
    }
}
