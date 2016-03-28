package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;
import ispb.base.service.account.PaymentService;
import ispb.base.service.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddPaymentRpc extends RpcProcedure {

    private static class AddPaymentArgs extends RpcArg {

        private long customerId;
        private double sum;
        private String comment;

        public boolean verify() {
            return getCustomerId() > 0 && getComment() != null;
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
    }

    public int getAccessLevel(){
        return AccessLevel.KASS;
    }

    public Class<? extends RpcArg> getArgType() {
        return AddPaymentArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        PaymentService service = application.getByType(PaymentService.class);
        AddPaymentArgs args = (AddPaymentArgs)obj;
        try {
            service.addPayment(args.getCustomerId(), args.getSum(), args.getComment());
        }
        catch (NotFoundException e){
            return false;
        }
        return true;
    }
}
