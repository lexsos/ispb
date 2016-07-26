package ispb.base.radius.server;


import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.servlet.RadiusServletContext;

public interface RadiusAuthStrategy {
    RadiusPacket accessRequest(RadiusServletContext context);
}
