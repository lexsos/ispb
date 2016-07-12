package ispb.test.RadiusAttributes;

import ispb.base.radius.attribute.RadiusOctetAttr;
import ispb.base.radius.attribute.RadiusStringAttr;
import ispb.base.radius.attribute.RadiusVendorAttr;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;


public class TestRadiusVendorAttr  extends TestCase {

    public void testReadAttr() {
        String attrHex = "0x1a1100003a8c130b54617269666631304d";
        byte[] data = HexCodec.hexToByte(attrHex);

        RadiusOctetAttr octetAttr = new RadiusOctetAttr();
        octetAttr.readValue(2, 15, data);

        RadiusStringAttr strAttr = new RadiusStringAttr();
        strAttr.readValue(6, octetAttr.getRawValue().length - 6, octetAttr.getRawValue());

        assertTrue(strAttr.getValue().equals("Tariff10M"));

        RadiusVendorAttr vendorAttr = new RadiusVendorAttr(0, strAttr);
        assertTrue(vendorAttr.getValue().equals("Tariff10M"));
    }

    public void testWriteAttr(){
        RadiusStringAttr strAttr = new RadiusStringAttr();
        strAttr.setType(19);
        try {
            strAttr.setValue("Tariff10M");
        }
        catch (Throwable throwable){
            assertTrue(false);
        }

        RadiusVendorAttr vendorAttr = new RadiusVendorAttr(14988, strAttr);

        assertTrue(vendorAttr.getLength() == 17);
        byte[] data = new byte[17];
        int writeLen = vendorAttr.write(0, data);
        assertTrue(writeLen == 17);

        String hexData = HexCodec.byteToHex(data);
        assertTrue(hexData.equals("0x1a1100003a8c130b54617269666631304d"));
    }

}
