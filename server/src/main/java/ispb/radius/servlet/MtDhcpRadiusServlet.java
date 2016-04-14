package ispb.radius.servlet;


import ispb.base.radius.RadiusServlet;
import org.tinyradius.packet.RadiusPacket;

import java.net.InetAddress;

public class MtDhcpRadiusServlet extends RadiusServlet {

    public RadiusPacket service(RadiusPacket request, InetAddress clientAddress, String sharedSecret){
        // TODO: write implementation
        RadiusPacket response = makeAccessAccept(request);

        response.addAttribute("Mikrotik-Address-List", "Tariff10M");
        response.addAttribute("Mikrotik-Address-List", "AllowUsers");
        response.addAttribute("Session-Timeout", "500");
        response.addAttribute("Framed-IP-Address", "192.168.2.123");

        return response;
    }

    public String getSharedSecret(InetAddress remoteAddress){
        // TODO: write implementation
        return "123456";
    }
}
