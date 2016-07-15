package ispb.base.radius.middleware;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;

public class RadiusProxyStateMiddle implements RadiusMiddleOut {

    public static final int TYPE_PROXY_STATE = 33;

    @SuppressWarnings("Convert2streamapi")
    public void out(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException {
        for (RadiusAttribute attribute: request.getAttributeList())
            if (attribute.getType() == TYPE_PROXY_STATE)
                reply.addAttribute(attribute);
    }
}
