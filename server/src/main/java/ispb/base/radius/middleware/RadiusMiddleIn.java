package ispb.base.radius.middleware;

import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;


public interface RadiusMiddleIn {
    default void in(RadiusPacket request, byte[] secret) throws RadiusException{
    }

    default void in(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{
    }
}
