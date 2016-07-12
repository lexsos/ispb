package ispb.test.Ip4Address;


import ispb.base.utils.Ip4Address;
import junit.framework.TestCase;

public class TestIp4Address_AsBytes extends TestCase {

    public void testIncorrectInput(){
        byte[] data;

        data = Ip4Address.asBytes("256.0.0.1");
        assertTrue( data == null );

        data = Ip4Address.asBytes("0.0.0.-1");
        assertTrue( data == null );

        data = Ip4Address.asBytes("0.0.0.256");
        assertTrue( data == null );
    }


    public void testAllZero(){
        byte[] data;

        data = Ip4Address.asBytes("0.0.0.0");
        assertTrue( data != null );
        assertTrue( data[0] == 0 );
        assertTrue( data[1] == 0 );
        assertTrue( data[2] == 0 );
        assertTrue( data[3] == 0 );

    }

    public void testAll255(){
        byte[] data;

        data = Ip4Address.asBytes("255.255.255.255");
        assertTrue( data != null );
        assertTrue( (data[0] & 0x0ff) == 255 );
        assertTrue( (data[1] & 0x0ff) == 255 );
        assertTrue( (data[2] & 0x0ff) == 255 );
        assertTrue( (data[3] & 0x0ff) == 255 );
    }

    public void testVarInput(){
        byte[] data;

        data = Ip4Address.asBytes("10.1.2.3");
        assertTrue( data != null );
        assertTrue( (data[0] & 0x0ff) == 10 );
        assertTrue( (data[1] & 0x0ff) == 1 );
        assertTrue( (data[2] & 0x0ff) == 2 );
        assertTrue( (data[3] & 0x0ff) == 3 );

        data = Ip4Address.asBytes("172.16.255.3");
        assertTrue( data != null );
        assertTrue( (data[0] & 0x0ff) == 172 );
        assertTrue( (data[1] & 0x0ff) == 16 );
        assertTrue( (data[2] & 0x0ff) == 255 );
        assertTrue( (data[3] & 0x0ff) == 3 );

        data = Ip4Address.asBytes("128.64.32.16");
        assertTrue( data != null );
        assertTrue( (data[0] & 0x0ff) == 128 );
        assertTrue( (data[1] & 0x0ff) == 64 );
        assertTrue( (data[2] & 0x0ff) == 32 );
        assertTrue( (data[3] & 0x0ff) == 16 );
    }

}
