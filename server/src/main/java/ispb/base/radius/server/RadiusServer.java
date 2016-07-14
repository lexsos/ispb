package ispb.base.radius.server;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.fieldtype.RadiusClientType;

import java.util.List;

public interface RadiusServer {
    void start();
    void stop();

    void addServletType(RadiusClientType type, RadiusServlet servlet);
    void loadRadiusClient(List<RadiusClientDataSet> clientList);
}
