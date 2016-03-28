package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.CustomerAccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContractNumberExistRpc extends RpcProcedure {

    private static class ContractNumberExistArgs extends RpcArg {

        private String contractNumber;

        public boolean verify() {
            return getContractNumber() != null;
        }

        public String getContractNumber() {
            return contractNumber;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class<? extends RpcArg> getArgType() {
        return  ContractNumberExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        ContractNumberExistArgs arg = (ContractNumberExistArgs)obj;
        CustomerAccountService service = application.getByType(CustomerAccountService.class);
        return service.contractNumberExist(arg.getContractNumber());
    }
}
