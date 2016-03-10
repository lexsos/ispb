package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.dictionary.TariffDictionaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TariffNameExistRpc extends RpcProcedure {

    private static class TariffNameExistArgs extends RpcArg {

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
        return TariffNameExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        TariffNameExistArgs args = (TariffNameExistArgs)obj;
        return application.getByType(TariffDictionaryService.class).exist(args.getName());
    }
}
