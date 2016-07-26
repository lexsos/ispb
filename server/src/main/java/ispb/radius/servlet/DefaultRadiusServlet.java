package ispb.radius.servlet;

import ispb.base.Application;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.server.RadiusAuthStrategy;
import ispb.base.radius.servlet.RadiusServlet;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.radius.strategy.RadiusDefaultAuthStrategy;


public class DefaultRadiusServlet extends RadiusServlet {

    private final RadiusAuthStrategy authStrategy;

    public DefaultRadiusServlet(Config config){
        authStrategy = new RadiusDefaultAuthStrategy(config);
    }

    public DefaultRadiusServlet(Application application){
        this(application.getByType(Config.class));
    }

    protected RadiusPacket access(RadiusServletContext context){
        return authStrategy.accessRequest(context);
    }
}
