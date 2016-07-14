package ispb.base.radius.middleware;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class RadiusResponseAuthenticatorMiddle implements RadiusMiddleOut {

    private static final Set<Integer> PACKET_TYPES = new ConcurrentSkipListSet<>();

    static {
        PACKET_TYPES.add(RadiusPacket.ACCESS_ACCEPT);
        PACKET_TYPES.add(RadiusPacket.ACCESS_REJECT);
        PACKET_TYPES.add(RadiusPacket.ACCESS_CHALLENGE);
    }

    public void out(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException {
        if (!PACKET_TYPES.contains(reply.getPacketType()))
            return;

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e){
            throw new RadiusException();
        }

        md5.reset();
        md5.update((byte)(reply.getPacketType() & 0x0ff));
        md5.update((byte)(reply.getIdentifier() & 0x0ff));
        md5.update((byte)((reply.getLength() >> 8)  & 0x0ff));
        md5.update((byte)(reply.getLength() & 0x0ff));
        md5.update(request.getAuthenticator(), 0, request.getAuthenticator().length);

        byte[] attributeData = new byte[256];
        for (RadiusAttribute attribute: reply.getAttributeList()){
            int length = attribute.write(0, attributeData);
            md5.update(attributeData, 0, length);
        }

        md5.update(secret, 0, secret.length);

        reply.setAuth(md5.digest());
    }
}
