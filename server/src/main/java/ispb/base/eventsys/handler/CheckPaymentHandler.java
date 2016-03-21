package ispb.base.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.service.account.PaymentService;


public class CheckPaymentHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        PaymentService service = getPaymentService(application);
        service.applyNewPayments();
    }

    private PaymentService getPaymentService(Application application){
        return application.getByType(PaymentService.class);
    }

}
