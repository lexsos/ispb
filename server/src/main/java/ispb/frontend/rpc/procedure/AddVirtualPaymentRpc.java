package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.PaymentService;
import ispb.base.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class AddVirtualPaymentRpc extends RpcProcedure {

    private static class AddVirtualPaymentArgs extends RpcArg {

        private long customerId;
        private double sum;
        private String comment;
        private Date until;

        public boolean verify() {
            if (customerId > 0 && comment != null && until != null)
                return true;
            return false;
        }

        public long getCustomerId() {
            return customerId;
        }

        public double getSum() {
            return sum;
        }

        public String getComment() {
            return comment;
        }

        public Date getUntil() {
            return until;
        }

    }

    public int getAccessLevel(){
        return AccessLevel.KASS;
    }

    public Class getArgType() {
        return AddVirtualPaymentArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        PaymentService service = application.getByType(PaymentService.class);
        AddVirtualPaymentArgs args = (AddVirtualPaymentArgs)obj;
        try {
            service.addVirtualPayment(args.getCustomerId(),args.getSum(), args.getUntil(), args.getComment());
        }
        catch (ServiceException e){
            return false;
        }
        return true;
    }
}
