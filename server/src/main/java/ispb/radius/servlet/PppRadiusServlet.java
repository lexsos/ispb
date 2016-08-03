package ispb.radius.servlet;

import ispb.base.Application;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.server.RadiusAccountStrategy;
import ispb.base.radius.server.RadiusAuthStrategy;
import ispb.base.radius.servlet.RadiusServlet;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.radius.strategy.RadiusPppAccountStrategy;
import ispb.radius.strategy.RadiusPppAuthStrategy;


public class PppRadiusServlet extends RadiusServlet {

    private final RadiusAuthStrategy authStrategy;
    private final RadiusAccountStrategy accountStrategy;

    public PppRadiusServlet(Application application){

        Config config = application.getByType(Config.class);
        LogService logService = application.getByType(LogService.class);

        authStrategy = new RadiusPppAuthStrategy(config, logService, application);
        accountStrategy = new RadiusPppAccountStrategy(logService, application);
    }

    protected RadiusPacket access(RadiusServletContext context){
        return authStrategy.accessRequest(context);
    }

    protected RadiusPacket accounting(RadiusServletContext context){
        return accountStrategy.accountRequest(context);
    }

}
