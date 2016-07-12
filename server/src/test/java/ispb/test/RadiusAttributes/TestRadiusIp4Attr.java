package ispb.test.RadiusAttributes;

import ispb.base.radius.attribute.RadiusIp4Attr;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;


public class TestRadiusIp4Attr extends TestCase {

    public void testReadAttr(){
        String attrHex = "0x04060a016132";
        byte[] data = HexCodec.hexToByte(attrHex);

        RadiusIp4Attr attr = new RadiusIp4Attr();
        attr.readValue(2, 4, data);
        assertTrue(attr.getValue().equals("10.1.97.50"));
    }

    public void testWriteAttr(){
        RadiusIp4Attr attr = new RadiusIp4Attr();
        attr.setType(4);
        try {
            attr.setValue("10.1.97.50");
        }
        catch (Throwable throwable){
            assertTrue(false);
        }

        assertTrue(attr.getLength() == 6);
        byte[] data = new byte[6];
        int writeLen = attr.write(0, data);
        assertTrue(writeLen == 6);

        String hexData = HexCodec.byteToHex(data);
        assertTrue(hexData.equals("0x04060a016132"));
    }
}
