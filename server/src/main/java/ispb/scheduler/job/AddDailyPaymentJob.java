package ispb.scheduler.job;

import ispb.base.Application;
import ispb.base.scheduler.BaseJob;
import ispb.base.eventsys.EventSystem;
import ispb.base.eventsys.message.AddDailyPaymentMsg;


public class AddDailyPaymentJob extends BaseJob {

    public void execute(Application application) {
        EventSystem eventSystem = application.getByType(EventSystem.class);
        eventSystem.pushMessage(new AddDailyPaymentMsg());
    }
}
