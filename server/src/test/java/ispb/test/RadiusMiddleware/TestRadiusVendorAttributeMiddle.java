package ispb.test.RadiusMiddleware;

import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.attribute.RadiusOctetAttr;
import ispb.base.radius.attribute.RadiusStringAttr;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.dictionary.RadiusMemoryDictionary;
import ispb.base.radius.middleware.RadiusVendorAttributeMiddle;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;


public class TestRadiusVendorAttributeMiddle extends TestCase {

    public void testReadVendorAttribute(){
        RadiusDictionary dictionary = new RadiusMemoryDictionary();
        try {
            dictionary.addVendorAttribute("Address-List", 14988, 19, RadiusStringAttr.class);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        String attrHex = "0x1a1100003a8c130b54617269666631304d";
        byte[] data = HexCodec.hexToByte(attrHex);

        RadiusOctetAttr octetAttr = new RadiusOctetAttr();
        octetAttr.readValue(2, 15, data);

        RadiusVendorAttributeMiddle middle = new RadiusVendorAttributeMiddle(dictionary);
        RadiusAttribute vendorAttribute = null;
        try {
            vendorAttribute = middle.readVendorAttribute(octetAttr);
        }
        catch (Throwable e){
            assertTrue(false);
        }

        assertEquals(vendorAttribute.getValue(), "Tariff10M");
    }
}
