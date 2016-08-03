package ispb.radius.strategy;

import ispb.base.db.dataset.RadiusSessionAttributeDataSet;
import ispb.base.db.dataset.RadiusSessionDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.attribute.RadiusAttributeContainer;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.servlet.RadiusServletContext;
import ispb.base.service.LogService;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.InvalidIpAddressException;
import ispb.base.service.exception.NotFoundException;
import ispb.base.utils.DateUtils;
import ispb.base.utils.HexCodec;
import ispb.base.utils.Ip4Address;

import java.util.Date;
import java.util.Objects;

public class RadiusStrategyHelper {

    public static int getSessionTimeout(RadiusAttributeList responseAttributes){
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

    // TODO: foo dual stack (IPv4 + IPv6) change find address algorithm
    public static void saveSessionIp(RadiusSessionDataSet session,
                                     RadiusSessionService sessionService,
                                     LogService logService,
                                     RadiusAttributeList responseAttributes){
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

    public static void saveSessionAttributes(RadiusSessionDataSet session,
                                             RadiusSessionService sessionService,
                                             LogService logService,
                                             RadiusPacket packet){
        Date now = new Date();
        RadiusAttributeList attributeList = new RadiusAttributeList(packet);
        for (RadiusAttributeContainer attribute: attributeList){

            String attributeName = attribute.getAttributeName();
            String attributeValue = attribute.getAttributeValue();

            if (attributeName == null || attributeValue == null)
                continue;

            RadiusSessionAttributeDataSet attributeDataSet = new RadiusSessionAttributeDataSet();
            attributeDataSet.setPacketAt(now);
            attributeDataSet.setPacketType(packet.getPacketType());
            attributeDataSet.setSession(session);
            attributeDataSet.setAttribute(attributeName);
            attributeDataSet.setValue(attributeValue);

            try {
                sessionService.addAttribute(attributeDataSet);
            }catch (NotFoundException e){
                logService.warn("Can't save RADIUS attribute", e);
            }
        }
    }

    public static boolean connectionLimitReach(RadiusServletContext context,
                                               RadiusSessionService sessionService,
                                               RadiusUserDataSet user){

        if (context.getServlet().parameterEq("LimitConnections", "true")){
            int period =  Integer.parseInt(context.getServlet().getParameter("LimitPeriod"));
            int limit =  Integer.parseInt(context.getServlet().getParameter("LimitConnectionsCount"));
            return sessionService.getSessionInPeriod(user, period) >= limit;
        }
        return false;
    }

    public static RadiusSessionDataSet createSession(RadiusServletContext context,
                                                      RadiusUserDataSet user,
                                                     RadiusSessionService sessionService,
                                                     int sessionTimeout) throws NotFoundException{

        Date now = new Date();
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
        session = sessionService.createSession(session);
        return session;
    }

    public static void addSessionId(RadiusSessionService sessionService,
                                    RadiusPacket response,
                                    RadiusSessionDataSet session,
                                    RadiusDictionary dictionary){
        String sessionPattern = sessionService.getSessionPattern(session);
        RadiusAttribute attribute = dictionary.getType("Class").newInstance();
        attribute.setRawValue(HexCodec.stringToByte(sessionPattern));
        response.addAttribute(attribute);
    }

    public static RadiusSessionDataSet findSession(RadiusSessionService sessionService,
                                                   RadiusPacket packet){
        for (RadiusAttribute attribute: packet.getAttributeList())
            if (Objects.equals(attribute.getName(), "Class")){
                String value = HexCodec.byteToString(attribute.getRawValue());
                RadiusSessionDataSet session = sessionService.getSessionByPattern(value);
                if (session != null)
                    return session;
            }
        return null;
    }

    public static void saveUserIp(RadiusAttributeList requestAttributes,
                                  RadiusUserService userService,
                                  RadiusSessionDataSet session,
                                  LogService logService){
        String ip = requestAttributes.getFirstValue("Framed-IP-Address");
        if (ip == null)
            return;

        ip = Ip4Address.normalize(ip);
        if (ip == null) {
            logService.warn("Can't save IP address for RADIUS user, invalid ip address");
            return;
        }

        if (userService.ip4Exist(ip)) {
            logService.warn("Can't save IP address for RADIUS user, ip address already used " + ip);
            return;
        }

        RadiusUserDataSet user = session.getRadiusUser();
        user.setIp4Address(ip);
        try {
            userService.update(user.getId(), user);
        }catch (Throwable throwable){
            logService.warn("Can't save IP address for RADIUS user", throwable);
        }
    }

}
