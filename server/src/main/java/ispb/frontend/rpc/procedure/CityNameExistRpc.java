package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.CityDictionaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CityNameExistRpc extends RpcProcedure {

    private static class CityNameExistArgs extends RpcArg {

        private String name;

        public boolean verify() {
            if (getName() != null)
                return true;
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class getArgType(){
        return CityNameExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        CityNameExistArgs args = (CityNameExistArgs)obj;
        return application.getByType(CityDictionaryService.class).exist(args.getName());
    }
}
