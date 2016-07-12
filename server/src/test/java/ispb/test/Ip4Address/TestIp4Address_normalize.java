package ispb.test.Ip4Address;

import ispb.base.utils.Ip4Address;
import junit.framework.TestCase;


public class TestIp4Address_normalize extends TestCase {

    public void testIncorrectInput(){
        String addr;

        addr= Ip4Address.normalize("-1.0.0.0");
        assertTrue(addr == null);

        addr= Ip4Address.normalize("256.0.0.0");
        assertTrue(addr == null);

        addr= Ip4Address.normalize("0.0.0.256");
        assertTrue(addr == null);

        addr= Ip4Address.normalize("0.0.0");
        assertTrue(addr == null);

        addr= Ip4Address.normalize("0.0.0.0.0");
        assertTrue(addr == null);
    }

    public void testZero(){
        String addr;

        addr= Ip4Address.normalize("0.0.0.0");
        assertTrue(addr.equals("0.0.0.0"));

        addr= Ip4Address.normalize("000.000.000.000");
        assertTrue(addr.equals("0.0.0.0"));

        addr= Ip4Address.normalize("0000.0000.0000.0000");
        assertTrue(addr.equals("0.0.0.0"));

        addr= Ip4Address.normalize("0001.0001.0001.0001");
        assertTrue(addr.equals("1.1.1.1"));

        addr= Ip4Address.normalize("000255.000255.000255.000255");
        assertTrue(addr.equals("255.255.255.255"));

        addr= Ip4Address.normalize("000128.000128.000128.000128");
        assertTrue(addr.equals("128.128.128.128"));

        addr= Ip4Address.normalize("000128.00064.00032.00016");
        assertTrue(addr.equals("128.64.32.16"));
    }

    public void testCorrectInput(){
        String addr;

        addr= Ip4Address.normalize("10.1.2.3");
        assertTrue(addr.equals("10.1.2.3"));

        addr= Ip4Address.normalize("172.16.100.200");
        assertTrue(addr.equals("172.16.100.200"));

        addr= Ip4Address.normalize("192.168.1.2");
        assertTrue(addr.equals("192.168.1.2"));
    }
}
