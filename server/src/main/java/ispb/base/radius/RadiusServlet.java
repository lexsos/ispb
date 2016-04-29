package ispb.base.radius;


import ispb.base.db.dataset.RadiusClientDataSet;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;

import java.net.InetAddress;

public class RadiusServlet {

    public RadiusPacket service(RadiusPacket request, RadiusClientDataSet clientDataSet){

        if (request instanceof AccessRequest)
            return access((AccessRequest)request, clientDataSet);
        else if (request instanceof AccountingRequest)
            return accounting((AccountingRequest)request, clientDataSet);

        return null;
    }

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    protected RadiusPacket access(AccessRequest request, RadiusClientDataSet clientDataSet){
        return null;
    }

    protected RadiusPacket accounting(AccountingRequest request, RadiusClientDataSet clientDataSet){
        return null;
    }

    protected RadiusPacket makeAccessAccept(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_ACCEPT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccessReject(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_REJECT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccountingResponse(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCOUNTING_RESPONSE, request.getPacketIdentifier());
    }
}
