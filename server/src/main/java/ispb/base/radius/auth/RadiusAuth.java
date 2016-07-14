package ispb.base.radius.auth;


import ispb.base.radius.packet.RadiusPacket;

public interface RadiusAuth {
    boolean checkPassword(RadiusPacket packet, String plainPassword);
}
