package ispb.frontend.rpc.procedure;


import ispb.base.Application;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.CustomerStatusService;
import ispb.base.service.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class PlaneSuspenseCustomerRpc extends RpcProcedure {

    private static class PlaneSuspenseCustomerArgs extends RpcArg {

        private long customerId;
        private Date dateSuspend;
        private Date dateResume;

        public boolean verify(){
            return getCustomerId() > 0 && getDateSuspend() != null && getDateResume() != null;
        }

        public long getCustomerId() {
            return customerId;
        }

        public Date getDateSuspend() {
            return dateSuspend;
        }

        public Date getDateResume() {
            return dateResume;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MANAGER;
    }

    public Class<? extends RpcArg> getArgType(){
        return PlaneSuspenseCustomerArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        CustomerStatusService service = application.getByType(CustomerStatusService.class);
        PlaneSuspenseCustomerArgs args = (PlaneSuspenseCustomerArgs)obj;

        if (!args.getDateSuspend().before(args.getDateResume()))
            return false;

        try {
            service.managerPlaneStatus(args.getCustomerId(), CustomerStatus.INACTIVE, args.getDateSuspend());
            service.managerPlaneStatus(args.getCustomerId(), CustomerStatus.ACTIVE, args.getDateResume());
            return true;
        }
        catch (NotFoundException e){
            return false;
        }
    }
}
