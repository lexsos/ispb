package ispb.test.RadiusMiddleware;

import ispb.base.radius.middleware.RadiusUserPasswordMiddle;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;


public class TestRadiusUserPasswordMiddle extends TestCase {

    private RadiusUserPasswordMiddle middle = new RadiusUserPasswordMiddle();

    public void testDecryptEmptyPassword(){
        byte[] authenticator = HexCodec.hexToByte("0x51dcb074ff5c49194a94e82aec585562");
        byte[] sharedSecret = HexCodec.stringToByte("123456");
        byte[] encryptedPassword = HexCodec.hexToByte("0xf402641a0fd400e268a23832f3497386");

        String password = null;
        try {
            password = middle.decodePapPassword(encryptedPassword, sharedSecret, authenticator);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        assertEquals(password, "");
    }

    public void testEncryptEmptyPassword(){
        byte[] authenticator = HexCodec.hexToByte("0x51dcb074ff5c49194a94e82aec585562");
        byte[] sharedSecret = HexCodec.stringToByte("123456");

        byte[] encryptedPassword = null;
        try {
            encryptedPassword = middle.encodePapPassword(HexCodec.stringToByte(""), sharedSecret, authenticator);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        assertEquals(HexCodec.byteToHex(encryptedPassword), "0xf402641a0fd400e268a23832f3497386");
    }

    public void testDecryptPassword(){
        byte[] authenticator = HexCodec.hexToByte("0xeab96324dc4a885e6bd3ea519677512d");
        byte[] sharedSecret = HexCodec.stringToByte("123456");
        byte[] encryptedPassword = HexCodec.hexToByte("0x7404695da9d1a86fb4afab7a57abc610");

        String password = null;
        try {
            password = middle.decodePapPassword(encryptedPassword, sharedSecret, authenticator);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        assertEquals(password, "123456");
    }

    public void testEncryptPassword(){
        byte[] authenticator = HexCodec.hexToByte("0xeab96324dc4a885e6bd3ea519677512d");
        byte[] sharedSecret = HexCodec.stringToByte("123456");
        byte[] password = HexCodec.stringToByte("123456");

        byte[] encryptedPassword = null;
        try {
            encryptedPassword = middle.encodePapPassword(password, sharedSecret, authenticator);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        assertEquals(HexCodec.byteToHex(encryptedPassword), "0x7404695da9d1a86fb4afab7a57abc610");
    }
}
