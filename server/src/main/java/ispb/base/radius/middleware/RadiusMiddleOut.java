package ispb.base.radius.middleware;

import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;


public interface RadiusMiddleOut {
    default void out(RadiusPacket request, byte[] secret) throws RadiusException{

    }

    default void out(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{

    }
}
