package ispb.test.Ip4Address;

import ispb.base.utils.Ip4Address;
import junit.framework.TestCase;


public class TestIp4Address_fromBytes extends TestCase {

    public void testAllZero(){
        byte[] data = new byte[4];
        data[0] = 0;
        data[1] = 0;
        data[2] = 0;
        data[3] = 0;

        String addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("0.0.0.0"));
    }

    public void testAll255(){
        byte[] data = new byte[4];
        data[0] = (byte)(255 & 0x0ff);
        data[1] = (byte)(255 & 0x0ff);
        data[2] = (byte)(255 & 0x0ff);
        data[3] = (byte)(255 & 0x0ff);

        String addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("255.255.255.255"));
    }

    public void testVarInput(){
        byte[] data = new byte[4];
        String addr;

        data[0] = (byte)(10 & 0x0ff);
        data[1] = (byte)(1 & 0x0ff);
        data[2] = (byte)(2 & 0x0ff);
        data[3] = (byte)(3 & 0x0ff);
        addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("10.1.2.3"));

        data[0] = (byte)(128 & 0x0ff);
        data[1] = (byte)(64 & 0x0ff);
        data[2] = (byte)(32 & 0x0ff);
        data[3] = (byte)(16 & 0x0ff);
        addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("128.64.32.16"));

        data[0] = (byte)(172 & 0x0ff);
        data[1] = (byte)(16 & 0x0ff);
        data[2] = (byte)(100 & 0x0ff);
        data[3] = (byte)(200 & 0x0ff);
        addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("172.16.100.200"));

        data[0] = (byte)(127 & 0x0ff);
        data[1] = (byte)(0 & 0x0ff);
        data[2] = (byte)(0 & 0x0ff);
        data[3] = (byte)(1 & 0x0ff);
        addr = Ip4Address.fromBytes(data);
        assertTrue(addr.equals("127.0.0.1"));
    }
}
