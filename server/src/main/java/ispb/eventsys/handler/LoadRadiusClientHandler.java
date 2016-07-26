package ispb.eventsys.handler;

import ispb.base.Application;
import ispb.base.eventsys.EventHandler;
import ispb.base.eventsys.EventMessage;
import ispb.base.radius.server.RadiusServer;
import ispb.base.radius.servlet.RadiusClientRepositoryBuilder;
import ispb.base.service.dictionary.RadiusClientDictionaryService;

public class LoadRadiusClientHandler implements EventHandler {

    public void run(Application application, EventMessage message){
        RadiusClientRepositoryBuilder repositoryBuilder = getRepositoryBuilder(application);
        RadiusServer radiusServer = getRadiusServer(application);

        radiusServer.setClientRepository(repositoryBuilder.buildRepository());
    }

    private RadiusServer getRadiusServer(Application application){
        return application.getByType(RadiusServer.class);
    }

    private RadiusClientRepositoryBuilder getRepositoryBuilder(Application application){
        return application.getByType(RadiusClientRepositoryBuilder.class);
    }
}
