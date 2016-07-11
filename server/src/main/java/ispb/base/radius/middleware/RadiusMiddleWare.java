package ispb.base.radius.middleware;


import ispb.base.radius.packet.RadiusPacket;

public interface RadiusMiddleWare {
    boolean in(RadiusPacket request, byte[] secret);
    boolean in(RadiusPacket request, RadiusPacket reply, byte[] secret);

    boolean out(RadiusPacket request, byte[] secret);
    boolean out(RadiusPacket request, RadiusPacket reply, byte[] secret);
}
