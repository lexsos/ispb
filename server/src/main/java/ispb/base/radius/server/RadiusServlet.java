package ispb.base.radius.server;

import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.radius.packet.RadiusPacket;

public class RadiusServlet {

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    public RadiusPacket service(RadiusServletContext context){
        RadiusPacket request = context.getRequest();

        if (request.getPacketType() == RadiusPacket.ACCESS_REQUEST)
            return access(context);
        else if (request.getPacketType() == RadiusPacket.ACCOUNTING_REQUEST)
            return accounting(context);

        return null;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    protected RadiusPacket accounting(RadiusServletContext context){
        return null;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    protected RadiusPacket access(RadiusServletContext context){
        return null;
    }
}
