package ispb.eventsys.job;


import ispb.base.Application;
import ispb.base.eventsys.BaseJob;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.CheckCustomerStatusMsg;

public class CheckNewCustomerStatusJob extends BaseJob {

    public void execute(Application application){
        EventSystem eventSystem = application.getByType(EventSystem.class);
        eventSystem.pushMessage(new CheckCustomerStatusMsg());
    }
}
