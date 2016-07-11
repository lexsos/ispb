package ispb.base.radius.io;


import ispb.base.radius.packet.RadiusPacket;

public interface RadiusPacketReader {
    RadiusPacket read(byte[] data);
}
