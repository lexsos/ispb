package ispb.radius.strategy;

import ispb.base.Application;
import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.packet.RadiusPacketBuilder;
import ispb.base.radius.server.RadiusAccountStrategy;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.service.LogService;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.service.account.RadiusUserService;


public class RadiusPppAccountStrategy implements RadiusAccountStrategy {

    private final Application application;
    private final LogService logService;

    public RadiusPppAccountStrategy(LogService logService, Application application){
        this.application = application;
        this.logService = logService;
    }

    public RadiusPacket accountRequest(RadiusServletContext context){

        RadiusSessionService sessionService = getSessionService();
        RadiusSessionDataSet session = RadiusStrategyHelper.findSession(sessionService, context.getRequest());

        if (session == null){
            logService.warn("Can't find RADIUS session for accounting request from: " + context.getClient().getIp4Address());
            return null;
        }

        RadiusAttributeList requestAttributes = new RadiusAttributeList(context.getRequest());
        int acctType;
        try {
            acctType = Integer.parseInt(requestAttributes.getFirstValue("Acct-Status-Type"));
        }catch (Throwable throwable){
            logService.warn("RADIUS accounting, invalid Acct-Status-Type from: " + context.getClient().getIp4Address(), throwable);
            return null;
        }

        if (acctType == RadiusPacket.ACCT_STATUS_TYPE_START)
            doStart(context, session, sessionService, requestAttributes);
        else if (acctType == RadiusPacket.ACCT_STATUS_TYPE_STOP)
            doStop(context, session, sessionService, requestAttributes);

        if (context.getServlet().parameterEq("SaveSessionAttributes", "true"))
            RadiusStrategyHelper.saveSessionAttributes(session, sessionService, logService, context.getRequest());

        return RadiusPacketBuilder.getAccountingResponse(context.getRequest());
    }

    private void doStop(RadiusServletContext context,
                        RadiusSessionDataSet session,
                        RadiusSessionService sessionService,
                        RadiusAttributeList requestAttributes){
        sessionService.closeSession(session);
    }

    private void doStart(RadiusServletContext context,
                        RadiusSessionDataSet session,
                        RadiusSessionService sessionService,
                        RadiusAttributeList requestAttributes){

        RadiusStrategyHelper.saveSessionIp(session, sessionService, logService, requestAttributes);

        if (context.getServlet().parameterEq("SaveUserIpAddress", "true") && session.getRadiusUser().getIp4Address() == null){
            RadiusUserService userService = application.getByType(RadiusUserService.class);
            RadiusStrategyHelper.saveUserIp(requestAttributes, userService, session, logService);
        }

        // TODO: save needed attribute for send disconnect packet to BRAS
    }

    private RadiusSessionService getSessionService(){
        return application.getByType(RadiusSessionService.class);
    }
}
