package ispb.base.radius;


import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;

import java.net.InetAddress;

public class RadiusServlet {

    public RadiusPacket service(RadiusPacket request, InetAddress clientAddress, String sharedSecret){

        if (request instanceof AccessRequest)
            return access((AccessRequest)request, clientAddress, sharedSecret);
        else if (request instanceof AccountingRequest)
            return accounting((AccountingRequest)request, clientAddress, sharedSecret);

        return null;
    }

    public String getSharedSecret(InetAddress remoteAddress){
        return null;
    }

    protected RadiusPacket access(AccessRequest request, InetAddress clientAddress, String sharedSecret){
        return null;
    }

    protected RadiusPacket accounting(AccountingRequest request, InetAddress clientAddress, String sharedSecret){
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
