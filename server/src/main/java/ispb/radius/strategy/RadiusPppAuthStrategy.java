package ispb.radius.strategy;


import ispb.base.Application;
import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.packet.RadiusPacketBuilder;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.service.exception.NotFoundException;


public class RadiusPppAuthStrategy extends RadiusDefaultAuthStrategy {

    private final Application application;
    private final LogService logService;

    public RadiusPppAuthStrategy(Config config, LogService logService, Application application){
        super(config);
        this.application = application;
        this.logService = logService;
    }

    protected RadiusPacket afterAuth(RadiusServletContext context, RadiusUserDataSet user, RadiusPacket response){

        if (response == null || response.getPacketType() != RadiusPacket.ACCESS_ACCEPT)
            return response;

        RadiusSessionService sessionService = getSessionService();

        if (RadiusStrategyHelper.connectionLimitReach(context, sessionService, user)) {
            logService.info("PPP RADIUS servlet send reject. Too many connections from: " + user.getUserName());
            return RadiusPacketBuilder.getAccessReject(context.getRequest());
        }

        RadiusAttributeList responseAttributes = new RadiusAttributeList(response);
        int sessionTimeout = RadiusStrategyHelper.getSessionTimeout(responseAttributes);
        RadiusSessionDataSet session;

        try {
            session = RadiusStrategyHelper.createSession(context, user, sessionService, sessionTimeout);
        } catch (NotFoundException e){
            logService.warn("Can't create RADIUS session fou user: " + user.getUserName());
            return response;
        }

        RadiusStrategyHelper.addSessionId(sessionService, response, session, getRadiusDictionary());
        RadiusStrategyHelper.saveSessionIp(session, sessionService, logService, responseAttributes);

        if (context.getServlet().parameterEq("SaveSessionAttributes", "true")) {
            RadiusStrategyHelper.saveSessionAttributes(session, sessionService, logService, context.getRequest());
            RadiusStrategyHelper.saveSessionAttributes(session, sessionService, logService, response);
        }

        return response;
    }

    private RadiusSessionService getSessionService(){
        return application.getByType(RadiusSessionService.class);
    }

    private RadiusDictionary getRadiusDictionary(){
        return application.getByType(RadiusDictionary.class);
    }
}
