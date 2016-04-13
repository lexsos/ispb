package ispb.base.radius;


import org.tinyradius.packet.RadiusPacket;

import java.net.InetAddress;

public class RadiusServlet {

    public RadiusPacket handle(RadiusPacket request, InetAddress clientAddress, String sharedSecret){
        return null;
    }

    public String getSharedSecret(InetAddress remoteAddress){
        return null;
    }
}
