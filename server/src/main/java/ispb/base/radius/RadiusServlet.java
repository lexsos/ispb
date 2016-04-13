package ispb.base.radius;


import org.tinyradius.packet.RadiusPacket;

import java.net.InetAddress;

public class RadiusServlet {

    public RadiusPacket service(RadiusPacket request, InetAddress clientAddress, String sharedSecret){
        return null;
    }

    public String getSharedSecret(InetAddress remoteAddress){
        return null;
    }

    protected RadiusPacket makeAccessAccept(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_ACCEPT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccessReject(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_REJECT, request.getPacketIdentifier());
    }
}
