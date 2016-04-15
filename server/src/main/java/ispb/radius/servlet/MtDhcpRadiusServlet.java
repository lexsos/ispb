package ispb.radius.servlet;


import ispb.base.radius.RadiusServlet;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import java.net.InetAddress;

public class MtDhcpRadiusServlet extends RadiusServlet {

    protected RadiusPacket access(AccessRequest request, InetAddress clientAddress, String sharedSecret){
        // TODO: write implementation

        try {

            if (request.verifyPassword("1234567")) {
                RadiusPacket response = makeAccessAccept(request);

                response.addAttribute("Mikrotik-Address-List", "Tariff10M");
                response.addAttribute("Mikrotik-Address-List", "AllowUsers");
                response.addAttribute("Session-Timeout", "500");
                response.addAttribute("Framed-IP-Address", "192.168.2.123");

                return response;
            }
            else
                return makeAccessReject(request);
        }
        catch (RadiusException e){
            return makeAccessReject(request);
        }
    }

    public String getSharedSecret(InetAddress remoteAddress){
        // TODO: write implementation
        return "123456";
    }
}
