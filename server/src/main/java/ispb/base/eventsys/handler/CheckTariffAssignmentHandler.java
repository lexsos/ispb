package ispb.base.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.service.account.TariffAssignmentService;


public class CheckTariffAssignmentHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        TariffAssignmentService service = getTariffService(application);
        service.applyNewAssignment();
    }

    private TariffAssignmentService getTariffService(Application application){
        return application.getByType(TariffAssignmentService.class);
    }
}
