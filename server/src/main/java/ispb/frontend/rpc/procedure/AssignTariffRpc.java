package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class AssignTariffRpc extends RpcProcedure {

    private static class AssignTariffArgs extends RpcArg {

        private long customerId;
        private long tariffId;
        private Date from;

        public boolean verify() {
            if (getFrom() != null && getCustomerId() > 0 && getTariffId() > 0)
                return true;
            return false;
        }

        public long getCustomerId() {
            return customerId;
        }

        public long getTariffId() {
            return tariffId;
        }

        public Date getFrom() {
            return from;
        }
    }

    public int getAccessLevel() {
        return AccessLevel.MANAGER;
    }

    public Class getArgType() {
        return AssignTariffArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        AssignTariffArgs args = (AssignTariffArgs)obj;
        TariffAssignmentService service = application.getByType(TariffAssignmentService.class);
        try {
            service.assignTariff(args.getCustomerId(), args.getTariffId(), args.getFrom());
            return true;
        }
        catch (ServiceException e){
            return false;
        }
    }
}
