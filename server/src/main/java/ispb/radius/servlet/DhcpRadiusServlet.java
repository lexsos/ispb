package ispb.radius.servlet;

import ispb.base.Application;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.server.RadiusAuthStrategy;
import ispb.base.radius.servlet.RadiusServlet;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.radius.strategy.RadiusDhcpAuthStrategy;


public class DhcpRadiusServlet extends RadiusServlet {

    private final RadiusAuthStrategy authStrategy;

    public DhcpRadiusServlet(Config config, LogService logService, Application application){
        authStrategy = new RadiusDhcpAuthStrategy(config, logService, application);
    }

    public DhcpRadiusServlet(Application application){
        this(application.getByType(Config.class), application.getByType(LogService.class), application);
    }

    protected RadiusPacket access(RadiusServletContext context){
        return authStrategy.accessRequest(context);
    }
}
