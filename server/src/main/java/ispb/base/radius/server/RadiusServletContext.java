package ispb.base.radius.server;


import ispb.base.Application;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.radius.RadiusAttributeList;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.service.LogService;

public class RadiusServletContext {

    private Application application;
    private RadiusPacket request;
    private RadiusClientDataSet client;
    private RadiusAttributeList attributeList;
    private LogService logService;


    public RadiusPacket getRequest() {
        return request;
    }

    public void setRequest(RadiusPacket request) {
        this.request = request;
    }

    public RadiusClientDataSet getClient() {
        return client;
    }

    public void setClient(RadiusClientDataSet client) {
        this.client = client;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public RadiusAttributeList getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(RadiusAttributeList attributeList) {
        this.attributeList = attributeList;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
