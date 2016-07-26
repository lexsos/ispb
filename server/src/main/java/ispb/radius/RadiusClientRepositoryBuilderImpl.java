package ispb.radius;


import ispb.base.Application;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusClientParameterDataSet;
import ispb.base.db.fieldtype.RadiusClientType;
import ispb.base.radius.servlet.RadiusServlet;
import ispb.base.radius.servlet.RadiusClientRepository;
import ispb.base.radius.servlet.RadiusClientRepositoryBuilder;
import ispb.base.service.LogService;
import ispb.base.service.dictionary.RadiusClientDictionaryService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RadiusClientRepositoryBuilderImpl implements RadiusClientRepositoryBuilder {

    private final LogService logService;
    private final Application application;
    private final Map<RadiusClientType, Class<? extends RadiusServlet>> servletMap = new ConcurrentHashMap<>();


    public RadiusClientRepositoryBuilderImpl(LogService logService, Application application){
        this.logService = logService;
        this.application = application;
    }

    public void addServletType(RadiusClientType type, Class<? extends RadiusServlet> clazz){
        servletMap.put(type, clazz);
    }

    public RadiusClientRepository buildRepository(){
        RadiusClientRepository repository = new RadiusClientRepository(logService);
        RadiusClientDictionaryService clientService = getClientService();

        List<RadiusClientDataSet> clientList = clientService.getRadiusClientList();
        for (RadiusClientDataSet client: clientList){
            Class<? extends RadiusServlet> clazz = servletMap.get(client.getClientType());

            if (clazz == null){
                logService.warn("Unknown RADIUS servlet type: " + client.getClientType());
                continue;
            }

            RadiusServlet servlet;
            try {
                servlet = clazz.getConstructor(Application.class).newInstance(application);
            }catch (Throwable e){
                logService.warn("Error while create new RADIUS servlet for client: " + client.getIp4Address(), e);
                continue;
            }

            loadParameters(client, servlet);

            repository.addClient(client, servlet);
        }
        return repository;
    }

    private void loadParameters(RadiusClientDataSet client, RadiusServlet servlet){
        RadiusClientDictionaryService clientService = getClientService();
        List<RadiusClientParameterDataSet> parameterList = clientService.getClientParameters(client.getId());

        for (RadiusClientParameterDataSet parameter: parameterList)
            servlet.setParameter(parameter.getParameter(), parameter.getValue());
    }

    private RadiusClientDictionaryService getClientService(){
        return application.getByType(RadiusClientDictionaryService.class);
    }
}
