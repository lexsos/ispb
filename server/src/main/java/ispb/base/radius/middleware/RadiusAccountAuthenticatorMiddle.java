package ispb.base.radius.middleware;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusBadAuthenticator;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.packet.RadiusPacket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RadiusAccountAuthenticatorMiddle implements RadiusMiddleIn, RadiusMiddleOut {

    public void in(RadiusPacket request, byte[] secret) throws RadiusException {

        if (request.getPacketType() != RadiusPacket.ACCOUNTING_REQUEST)
            return;

        MessageDigest md5 = getMd5();

        md5.reset();
        md5.update((byte)(request.getPacketType() & 0x0ff));
        md5.update((byte)(request.getIdentifier() & 0x0ff));
        md5.update((byte)((request.getLength() >> 8)  & 0x0ff));
        md5.update((byte)(request.getLength() & 0x0ff));

        for (int i=0; i<request.getAuthenticator().length; i++)
            md5.update((byte)0);

        byte[] attributeData = new byte[256];
        for (RadiusAttribute attribute: request.getAttributeList()){
            int length = attribute.write(0, attributeData);
            md5.update(attributeData, 0, length);
        }

        md5.update(secret);

        if (!Arrays.equals(request.getAuthenticator(), md5.digest()))
            throw new RadiusBadAuthenticator();
    }

    public void out(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{
        if (reply.getPacketType() != RadiusPacket.ACCOUNTING_RESPONSE)
            return;

        MessageDigest md5 = getMd5();

        md5.reset();
        md5.update((byte)(reply.getPacketType() & 0x0ff));
        md5.update((byte)(reply.getIdentifier() & 0x0ff));
        md5.update((byte)((reply.getLength() >> 8)  & 0x0ff));
        md5.update((byte)(reply.getLength() & 0x0ff));
        md5.update(request.getAuthenticator());

        byte[] attributeData = new byte[256];
        for (RadiusAttribute attribute: reply.getAttributeList()){
            int length = attribute.write(0, attributeData);
            md5.update(attributeData, 0, length);
        }

        md5.update(secret);

        reply.setAuth(md5.digest());
    }

    private MessageDigest getMd5() throws RadiusException{
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e){
            throw new RadiusException();
        }
        return md5;
    }
}
