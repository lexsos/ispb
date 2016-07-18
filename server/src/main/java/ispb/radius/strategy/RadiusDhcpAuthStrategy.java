package ispb.radius.strategy;


import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.packet.RadiusPacketBuilder;
import ispb.base.radius.server.RadiusServletContext;
import ispb.base.resources.Config;
import ispb.base.service.exception.ServiceException;

public class RadiusDhcpAuthStrategy extends RadiusDefaultAuthStrategy {

    public RadiusDhcpAuthStrategy(Config config){
        super(config);
    }

    protected RadiusPacket unknownUser(RadiusServletContext context, String userName){

        if (context.getClient().isAddAuthRequest()){
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
}
