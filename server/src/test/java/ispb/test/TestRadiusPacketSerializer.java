package ispb.test;

import ispb.base.radius.attribute.RadiusIntegerAttr;
import ispb.base.radius.attribute.RadiusIp4Attr;
import ispb.base.radius.attribute.RadiusStringAttr;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.dictionary.RadiusMemoryDictionary;
import ispb.base.radius.io.RadiusPacketSerializer;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.utils.HexCodec;
import junit.framework.TestCase;


public class TestRadiusPacketSerializer extends TestCase {

    private RadiusDictionary dictionary = new RadiusMemoryDictionary();
    private String packetData = "0x010d007333c2dc7f9fd7ef1bc9c4a7419a07686b3d060000000f0506838000071f15313a39633a64363a34333a36633a61363a66641e0973657276657231011339433a44363a34333a36433a41363a4644021299458c2a5854d3905b57a17816d3fdc4200a4d696b726f54696b04060a016132";
    private RadiusPacketSerializer serializer;


    protected void setUp() throws Exception {
        dictionary.addAttribute("NAS-Port-Type", 61, RadiusIntegerAttr.class);
        dictionary.addAttribute("NAS-Port", 5, RadiusIntegerAttr.class);
        dictionary.addAttribute("Calling-Station-Id", 31, RadiusStringAttr.class);
        dictionary.addAttribute("Called-Station-Id", 30, RadiusStringAttr.class);
        dictionary.addAttribute("User-Name", 1, RadiusStringAttr.class);
        dictionary.addAttribute("User-Password", 2, RadiusStringAttr.class);
        dictionary.addAttribute("NAS-Identifier", 32, RadiusStringAttr.class);
        dictionary.addAttribute("NAS-IP-Address", 4, RadiusIp4Attr.class);

        serializer = new RadiusPacketSerializer(dictionary);
    }

    private RadiusPacket readPacket(){
        byte data[] = HexCodec.hexToByte(packetData);
        try {
            return serializer.read(data);
        }
        catch (Throwable throwable){
            return null;
        }
    }

    public void testRead(){
        RadiusPacket packet = readPacket();

        assertTrue(packet != null);

        assertTrue(packet.getPacketType() == 1);
        assertTrue(packet.getIdentifier() == 13);

        assertTrue(packet.getAttributeList().size() == 8);

        assertTrue(packet.getAttributeList().get(0).getName().equals("NAS-Port-Type"));
        assertTrue(packet.getAttributeList().get(0).getValue().equals("15"));

        assertTrue(packet.getAttributeList().get(1).getName().equals("NAS-Port"));
        assertTrue(packet.getAttributeList().get(1).getValue().equals("2206203911"));

        assertTrue(packet.getAttributeList().get(2).getName().equals("Calling-Station-Id"));
        assertTrue(packet.getAttributeList().get(2).getValue().equals("1:9c:d6:43:6c:a6:fd"));

        assertTrue(packet.getAttributeList().get(3).getName().equals("Called-Station-Id"));
        assertTrue(packet.getAttributeList().get(3).getValue().equals("server1"));

        assertTrue(packet.getAttributeList().get(4).getName().equals("User-Name"));
        assertTrue(packet.getAttributeList().get(4).getValue().equals("9C:D6:43:6C:A6:FD"));

        assertTrue(packet.getAttributeList().get(5).getName().equals("User-Password"));

        assertTrue(packet.getAttributeList().get(6).getName().equals("NAS-Identifier"));
        assertTrue(packet.getAttributeList().get(6).getValue().equals("MikroTik"));

        assertTrue(packet.getAttributeList().get(7).getName().equals("NAS-IP-Address"));
        assertTrue(packet.getAttributeList().get(7).getValue().equals("10.1.97.50"));
    }

    public void testWrite(){
        RadiusPacket packet = readPacket();
        assertTrue(packet != null);

        byte[] data = serializer.write(packet);
        String hex = HexCodec.byteToHex(data);
        assertEquals(hex, packetData);
    }

}

