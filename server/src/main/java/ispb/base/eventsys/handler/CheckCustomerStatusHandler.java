package ispb.base.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.service.account.CustomerAccountService;


public class CheckCustomerStatusHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        CustomerAccountService service = getCustomerAccountService(application);
        service.applyNewStatuses();
    }

    private CustomerAccountService getCustomerAccountService(Application application){
        return application.getByType(CustomerAccountService.class);
    }
}
