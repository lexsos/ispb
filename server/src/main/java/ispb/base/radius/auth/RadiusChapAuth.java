package ispb.base.radius.auth;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.utils.HexCodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RadiusChapAuth implements RadiusAuth {

    public boolean checkPassword(RadiusPacket packet, String plainPassword){

        byte[] chapPassword = null;
        byte[] chapChallenge = packet.getAuthenticator();

        for (RadiusAttribute attribute: packet.getAttributeList()){
            if (attribute.getType() == RadiusPacket.ATTRIBUTE_CHAP_PASSWORD)
                chapPassword = attribute.getRawValue();
            if (attribute.getType() == RadiusPacket.ATTRIBUTE_CHAP_CHALLENGE)
                chapChallenge = attribute.getRawValue();
        }

        if (chapPassword == null || chapChallenge == null)
            return false;

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e){
            return false;
        }

        byte chapIdentifier = chapPassword[0];

        md5.reset();
        md5.update(chapIdentifier);
        md5.update(HexCodec.stringToByte(plainPassword));
        md5.update(chapChallenge);

        byte[] chapHash = md5.digest();

        if (chapHash.length != chapPassword.length - 1)
            return false;

        for (int i = 0; i < chapHash.length; i++)
            if (chapHash[i] != chapPassword[i + 1])
                return false;

        return true;
    }
}
