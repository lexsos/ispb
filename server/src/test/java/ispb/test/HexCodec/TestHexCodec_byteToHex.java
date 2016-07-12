package ispb.test.HexCodec;


import ispb.base.utils.HexCodec;
import junit.framework.TestCase;

public class TestHexCodec_byteToHex extends TestCase {

    public void testAllZero(){
        byte[] data1 = new byte[1];
        data1[0] = (byte)(0 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data1).equals("0x00"));

        byte[] data2 = new byte[2];
        data2[0] = (byte)(0 & 0x0ff);
        data2[1] = (byte)(0 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data2).equals("0x0000"));

        byte[] data4 = new byte[4];
        data4[0] = (byte)(0 & 0x0ff);
        data4[1] = (byte)(0 & 0x0ff);
        data4[2] = (byte)(0 & 0x0ff);
        data4[3] = (byte)(0 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data4).equals("0x00000000"));
    }

    public void testAllFf(){
        byte[] data1 = new byte[1];
        data1[0] = (byte)(255 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data1).equals("0xff"));

        byte[] data2 = new byte[2];
        data2[0] = (byte)(255 & 0x0ff);
        data2[1] = (byte)(255 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data2).equals("0xffff"));

        byte[] data4 = new byte[4];
        data4[0] = (byte)(255 & 0x0ff);
        data4[1] = (byte)(255 & 0x0ff);
        data4[2] = (byte)(255 & 0x0ff);
        data4[3] = (byte)(255 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data4).equals("0xffffffff"));
    }

    public void testVarInput(){
        byte[] data1 = new byte[1];
        data1[0] = (byte)(128 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data1).equals("0x80"));

        byte[] data2 = new byte[2];
        data2[0] = (byte)(127 & 0x0ff);
        data2[1] = (byte)(64 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data2).equals("0x7f40"));

        byte[] data4 = new byte[4];
        data4[0] = (byte)(32 & 0x0ff);
        data4[1] = (byte)(16 & 0x0ff);
        data4[2] = (byte)(8 & 0x0ff);
        data4[3] = (byte)(4 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data4).equals("0x20100804"));

        byte[] data5 = new byte[5];
        data5[0] = (byte)(3 & 0x0ff);
        data5[1] = (byte)(5 & 0x0ff);
        data5[2] = (byte)(7 & 0x0ff);
        data5[3] = (byte)(11 & 0x0ff);
        data5[4] = (byte)(17 & 0x0ff);
        assertTrue(HexCodec.byteToHex(data5).equals("0x0305070b11"));
    }
}
