package ispb.radius.strategy;

import ispb.base.Application;
import ispb.base.db.dataset.RadiusSessionAttributeDataSet;
import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.attribute.RadiusAttributeContainer;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.packet.RadiusPacketBuilder;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.service.exception.ServiceException;
import ispb.base.utils.DateUtils;

import java.util.Date;
import java.util.Objects;

public class RadiusDhcpAuthStrategy extends RadiusDefaultAuthStrategy {

    private final Application application;
    private final LogService logService;

    public RadiusDhcpAuthStrategy(Config config, LogService logService, Application application){
        super(config);
        this.application = application;
        this.logService = logService;
    }

    protected RadiusPacket unknownUser(RadiusServletContext context, String userName){

        if (context.getServlet().parameterEq("MakeAuthRequest", "true")){
            RadiusUserDataSet user = new RadiusUserDataSet();
            user.setUserName(userName);
            try {
                getUserService(context).create(user);
            }
            catch (ServiceException e){
                context.getLogService().warn("Can't add new RADIUS auth request for user " + userName, e);
            }
        }
        return RadiusPacketBuilder.getAccessReject(context.getRequest());
    }

    protected RadiusPacket afterAuth(RadiusServletContext context, RadiusUserDataSet user, RadiusPacket response){

        if (response == null || response.getPacketType() != RadiusPacket.ACCESS_ACCEPT)
            return response;

        RadiusSessionService sessionService = getSessionService();

        if (context.getServlet().parameterEq("LimitConnections", "true")){
            int period =  Integer.parseInt(context.getServlet().getParameter("LimitPeriod"));
            int limit =  Integer.parseInt(context.getServlet().getParameter("LimitConnectionsCount"));

            if (sessionService.getSessionInPeriod(user, period) >= limit) {
                logService.info("Send RADIUS reject. Too many connections from: " + user.getUserName());
                return RadiusPacketBuilder.getAccessReject(context.getRequest());
            }
        }

        RadiusAttributeList  responseAttributes = new RadiusAttributeList(response);

        Date now = new Date();
        int sessionTimeout = getSessionTimeout(responseAttributes);
        Date endSession = null;

        if (sessionTimeout > 0)
            endSession = DateUtils.addSecond(now, sessionTimeout);

        RadiusSessionDataSet session = new RadiusSessionDataSet();
        session.setCustomer(user.getCustomer());
        session.setRadiusUser(user);
        session.setRadiusClient(context.getClient());
        session.setStartAt(now);
        session.setExpireAt(endSession);
        session.setStopAt(endSession);

        try {
            session = sessionService.createSession(session);
        } catch (NotFoundException e){
            logService.warn("Can't create RADIUS session fou user: " + user.getUserName());
            return response;
        }

        addSessionIp(session, sessionService, logService, responseAttributes);
        addSessionAttributes(session, sessionService, logService, response);

        return response;
    }

    private RadiusSessionService getSessionService(){
        return application.getByType(RadiusSessionService.class);
    }

    private int getSessionTimeout(RadiusAttributeList responseAttributes){
        String timeout = responseAttributes.getFirstValue("Session-Timeout");
        if (timeout == null)
            return 0;
        try {
            return Integer.parseInt(timeout);
        }
        catch (Throwable throwable){
            return 0;
        }
    }

    private void addSessionIp(RadiusSessionDataSet session, RadiusSessionService sessionService, LogService logService, RadiusAttributeList responseAttributes){
        for (RadiusAttributeContainer attribute: responseAttributes){
            String attributeName = attribute.getAttributeName();
            String value = attribute.getAttributeValue();
            if (Objects.equals(attributeName, "Framed-IP-Address"))
                try {
                    sessionService.addSessionIp(session.getId(), value);
                }catch (NotFoundException e){
                    logService.warn("Can't add ip for RADIUS session, session not found", e);

                }catch (InvalidIpAddressException e){
                    logService.warn("Can't add ip for RADIUS session, invalid IP address: " + value, e);
                }
        }
    }

    private void addSessionAttributes(RadiusSessionDataSet session, RadiusSessionService sessionService, LogService logService, RadiusPacket packet){
        Date now = new Date();
        RadiusAttributeList attributeList = new RadiusAttributeList(packet);
        for (RadiusAttributeContainer attribute: attributeList){
            RadiusSessionAttributeDataSet attributeDataSet = new RadiusSessionAttributeDataSet();
            attributeDataSet.setPacketAt(now);
            attributeDataSet.setPacketType(packet.getPacketType());
            attributeDataSet.setSession(session);
            attributeDataSet.setAttribute(attribute.getAttributeName());
            attributeDataSet.setValue(attribute.getAttributeValue());

            try {
                sessionService.addAttribute(attributeDataSet);
            }catch (NotFoundException e){
                logService.warn("Can't save RADIUS attribute", e);
            }
        }
    }
}
