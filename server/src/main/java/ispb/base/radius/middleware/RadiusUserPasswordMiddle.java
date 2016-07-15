package ispb.base.radius.middleware;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.exception.RadiusPacketInvalidLength;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.utils.HexCodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RadiusUserPasswordMiddle implements RadiusMiddleIn, RadiusMiddleOut {

    public static final int TYPE_USER_PASSWORD = 2;

    private void md5Crypt(byte[] encryptedPass, byte[] sharedSecret, byte[] authenticator)  throws RadiusException {

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e){
            throw new RadiusException();
        }

        byte[] lastBlock = new byte[16];

        for (int i = 0; i < encryptedPass.length; i += 16) {
            md5.reset();
            md5.update(sharedSecret);
            md5.update(i == 0 ? authenticator : lastBlock);
            byte bn[] = md5.digest();

            System.arraycopy(encryptedPass, i, lastBlock, 0, 16);

            for (int j = 0; j < 16; j++)
                encryptedPass[i + j] = (byte) (bn[j] ^ encryptedPass[i + j]);
        }
    }

    public String decodePapPassword(byte[] encryptedPass, byte[] sharedSecret, byte[] authenticator) throws RadiusException {
        if (encryptedPass == null || encryptedPass.length < 16)
            throw new RadiusPacketInvalidLength();

        md5Crypt(encryptedPass, sharedSecret, authenticator);

        int len = encryptedPass.length;
        while (len > 0 && encryptedPass[len - 1] == 0)
            len--;
        byte[] passtrunc = new byte[len];
        System.arraycopy(encryptedPass, 0, passtrunc, 0, len);

        return HexCodec.byteToString(passtrunc);
    }

    public byte[] encodePapPassword(final byte[] userPass, byte[] sharedSecret, byte[] authenticator) throws RadiusException {

        byte[] userPassBytes;
        if (userPass.length > 128) {
            userPassBytes = new byte[128];
            System.arraycopy(userPass, 0, userPassBytes, 0, 128);
        }
        else
            userPassBytes = userPass;

        byte[] encryptedPass;
        if (userPassBytes.length < 128)
            if (userPassBytes.length % 16 == 0 && userPassBytes.length > 0)
                encryptedPass = new byte[userPassBytes.length];
            else
                encryptedPass = new byte[((userPassBytes.length / 16) * 16) + 16];
        else
            encryptedPass = new byte[128];

        System.arraycopy(userPassBytes, 0, encryptedPass, 0, userPassBytes.length);
        for (int i = userPassBytes.length; i < encryptedPass.length; i++)
            encryptedPass[i] = 0;

        md5Crypt(encryptedPass, sharedSecret, authenticator);

        return encryptedPass;
    }

    public void in(RadiusPacket request, byte[] secret) throws RadiusException {
        if (request.getPacketType() != RadiusPacket.ACCESS_REQUEST)
            return;

        for(RadiusAttribute attribute: request.getAttributeList())
            if (attribute.getType() == TYPE_USER_PASSWORD){
                String password = decodePapPassword(attribute.getRawValue(), secret, request.getAuthenticator());
                attribute.setValue(password);
            }
    }

    public void out(RadiusPacket request, byte[] secret) throws RadiusException {
        if (request.getPacketType() != RadiusPacket.ACCESS_REQUEST)
            return;

        for(RadiusAttribute attribute: request.getAttributeList())
            if (attribute.getType() == TYPE_USER_PASSWORD){
                byte[] password = encodePapPassword(attribute.getRawValue(), secret, request.getAuthenticator());
                attribute.setRawValue(password);
            }
    }
}
