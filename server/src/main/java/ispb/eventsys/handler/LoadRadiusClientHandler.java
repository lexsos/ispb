package ispb.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.radius.RadiusServer;
import ispb.base.service.dictionary.RadiusClientDictionaryService;

public class LoadRadiusClientHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        RadiusClientDictionaryService clientService = getClientService(application);
        RadiusServer radiusServer = getRadiusServer(application);
        radiusServer.loadRadiusClient(clientService.getRadiusClientList());
    }

    private RadiusServer getRadiusServer(Application application){
        return application.getByType(RadiusServer.class);
    }

    private RadiusClientDictionaryService getClientService(Application application){
        return application.getByType(RadiusClientDictionaryService.class);
    }
}
