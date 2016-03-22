package ispb.base.eventsys.handler;


import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.service.account.TariffPolicyService;
import ispb.base.utils.DateUtils;

import java.util.Date;

public class AddDailyPaymentHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        TariffPolicyService tariffPolicyService = getTariffPolicyService(application);
        Date yesterday = DateUtils.yesterday();
        tariffPolicyService.makeDailyPayment(yesterday);
    }

    private TariffPolicyService getTariffPolicyService(Application application){
        return application.getByType(TariffPolicyService.class);
    }
}
