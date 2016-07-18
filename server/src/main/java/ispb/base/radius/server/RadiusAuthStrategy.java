package ispb.base.radius.server;


import ispb.base.radius.packet.RadiusPacket;

public interface RadiusAuthStrategy {
    RadiusPacket accessRequest(RadiusServletContext context);
}
