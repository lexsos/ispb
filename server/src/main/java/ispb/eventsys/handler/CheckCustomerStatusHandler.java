package ispb.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.service.account.CustomerStatusService;


public class CheckCustomerStatusHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        CustomerStatusService service = getCustomerStatusService(application);
        service.applyNewStatuses();
    }

    private CustomerStatusService getCustomerStatusService(Application application){
        return application.getByType(CustomerStatusService.class);
    }
}
