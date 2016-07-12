package ispb.test.HexCodec;


import ispb.base.utils.HexCodec;
import junit.framework.TestCase;

public class TestHexCodec_hexToByte extends TestCase {

    public void testIncorectData(){
        assertTrue(  HexCodec.hexToByte(null) == null );
        assertTrue(  HexCodec.hexToByte("") == null );
        assertTrue(  HexCodec.hexToByte("1234567890") == null );
        assertTrue(  HexCodec.hexToByte("0x012") == null );
        assertTrue(  HexCodec.hexToByte("0xgg") == null );
        assertTrue(  HexCodec.hexToByte("0Xff") == null );
    }

    public void testAllZero(){
        byte[] data;

        data = HexCodec.hexToByte("0x00");
        assertTrue( data.length == 1 );
        assertTrue( data[0] == 0 );

        data = HexCodec.hexToByte("0x0000");
        assertTrue( data.length == 2 );
        assertTrue( data[0] == 0 );
        assertTrue( data[1] == 0 );

        data = HexCodec.hexToByte("0x00000000");
        assertTrue( data.length == 4 );
        assertTrue( data[0] == 0 );
        assertTrue( data[1] == 0 );
        assertTrue( data[2] == 0 );
        assertTrue( data[3] == 0 );
    }

    public void testAllFf(){
        byte[] data;

        data = HexCodec.hexToByte("0xFF");
        assertTrue( data.length == 1 );
        assertTrue( (data[0] & 0x0ff ) == 255 );

        data = HexCodec.hexToByte("0xFFFF");
        assertTrue( data.length == 2 );
        assertTrue( (data[0] & 0x0ff ) == 255 );
        assertTrue( (data[1] & 0x0ff ) == 255 );

        data = HexCodec.hexToByte("0xFFFFFFFF");
        assertTrue( data.length == 4 );
        assertTrue( (data[0] & 0x0ff ) == 255 );
        assertTrue( (data[1] & 0x0ff ) == 255 );
        assertTrue( (data[2] & 0x0ff ) == 255 );
        assertTrue( (data[3] & 0x0ff ) == 255 );
    }

    public void testVarInput(){
        byte[] data;

        data = HexCodec.hexToByte("0x0A");
        assertTrue( data.length == 1 );
        assertTrue( (data[0] & 0x0ff ) == 10 );

        data = HexCodec.hexToByte("0x0F");
        assertTrue( data.length == 1 );
        assertTrue( (data[0] & 0x0ff ) == 15 );

        data = HexCodec.hexToByte("0x0102");
        assertTrue( data.length == 2 );
        assertTrue( (data[0] & 0x0ff ) == 1 );
        assertTrue( (data[1] & 0x0ff ) == 2 );

        data = HexCodec.hexToByte("0xafEAdfCD");
        assertTrue( data.length == 4 );
        assertTrue( (data[0] & 0x0ff ) == 175 );
        assertTrue( (data[1] & 0x0ff ) == 234 );
        assertTrue( (data[2] & 0x0ff ) == 223 );
        assertTrue( (data[3] & 0x0ff ) == 205 );
    }

}
