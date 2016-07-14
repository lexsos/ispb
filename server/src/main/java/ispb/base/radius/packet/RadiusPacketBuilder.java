package ispb.base.radius.packet;


import java.security.SecureRandom;
import java.util.Random;

public class RadiusPacketBuilder {

    private static final Random random = new SecureRandom();

    public static void setRandomAuth(RadiusPacket packet){
        random.nextBytes(packet.getAuthenticator());
    }

    public static void setZeroAuth(RadiusPacket packet){
        byte[] authenticator = packet.getAuthenticator();
        for (int i=0; i<authenticator.length; i++)
            authenticator[i] = 0;
    }

    public static RadiusPacket getAccessRequest(){
        RadiusPacket packet = new RadiusPacket(RadiusPacket.ACCESS_REQUEST, 0);
        setRandomAuth(packet);
        return packet;
    }

    public static RadiusPacket getAccessAccept (RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_ACCEPT, request.getIdentifier());
     }

    public static RadiusPacket getAccessReject (RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_REJECT, request.getIdentifier());
    }
}
