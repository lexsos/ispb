package ispb.base.radius.io;


import ispb.base.radius.packet.RadiusPacket;

public interface RadiusPacketWriter {
    byte[] write(RadiusPacket radiusPacket);
}
