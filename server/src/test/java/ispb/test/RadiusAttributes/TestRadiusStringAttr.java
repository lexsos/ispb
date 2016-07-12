package ispb.test.RadiusAttributes;

import ispb.base.radius.attribute.RadiusStringAttr;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;

public class TestRadiusStringAttr extends TestCase {

    public void testReadAttr() {
        String attrHex = "0x200a4d696b726f54696b";
        byte[] data = HexCodec.hexToByte(attrHex);

        RadiusStringAttr attr = new RadiusStringAttr();
        attr.readValue(2, 8, data);
        assertTrue(attr.getValue().equals("MikroTik"));
    }

    public void testWriteAttr(){
        RadiusStringAttr attr = new RadiusStringAttr();
        attr.setType(32);
        try {
            attr.setValue("MikroTik");
        }
        catch (Throwable throwable){
            assertTrue(false);
        }

        assertTrue(attr.getLength() == 10);
        byte[] data = new byte[10];
        int writeLen = attr.write(0, data);
        assertTrue(writeLen == 10);

        String hexData = HexCodec.byteToHex(data);
        assertTrue(hexData.equals("0x200a4d696b726f54696b"));
    }
}