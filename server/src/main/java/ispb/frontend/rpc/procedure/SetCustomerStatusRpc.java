package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.CustomerStatusService;
import ispb.base.service.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetCustomerStatusRpc extends RpcProcedure {

    private static class SetCustomerStatusArgs extends RpcArg {

        private long customerId;
        private CustomerStatus status;

        public boolean verify() {
            if (getCustomerId() > 0 && getStatus() != null)
                return true;
            return false;
        }

        public long getCustomerId() {
            return customerId;
        }

        public CustomerStatus getStatus() {
            return status;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class getArgType() {
        return SetCustomerStatusArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        CustomerStatusService service = application.getByType(CustomerStatusService.class);
        SetCustomerStatusArgs args = (SetCustomerStatusArgs)obj;

        try {
            service.managerSetStatus(args.getCustomerId(), args.getStatus());
            return true;
        }
        catch (NotFoundException e){
            return false;
        }
    }
}
