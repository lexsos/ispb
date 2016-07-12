package ispb.test.RadiusAttributes;


import ispb.base.radius.attribute.RadiusIntegerAttr;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;

public class TestRadiusIntegerAttr extends TestCase {

    public void testReadAttr(){
        String attrHex = "0x280600000007";
        byte[] data = HexCodec.hexToByte(attrHex);

        RadiusIntegerAttr attr = new RadiusIntegerAttr();
        attr.readValue(2, 4, data);
        assertTrue(attr.getValue().equals("7"));
    }

    public void testWriteAttr(){
        RadiusIntegerAttr attr = new RadiusIntegerAttr();
        attr.setType(40);
        try {
            attr.setValue("7");
        }
        catch (Throwable throwable){
            assertTrue(false);
        }

        assertTrue(attr.getLength() == 6);
        byte[] data = new byte[6];
        int writeLen = attr.write(0, data);
        assertTrue(writeLen == 6);

        String hexData = HexCodec.byteToHex(data);
        assertTrue(hexData.equals("0x280600000007"));
    }

}
