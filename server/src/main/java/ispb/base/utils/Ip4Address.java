package ispb.base.utils;

public class Ip4Address {
    public static String normalize(String ipAddress){
        String[] ipAddressInArray = ipAddress.trim().split("\\.");

        if (ipAddressInArray.length != 4)
            return null;

        String result = "";

        for (int i=0; i<ipAddressInArray.length; i++){
            try {
                int octet = Integer.parseInt(ipAddressInArray[i]);
                if (0 <= octet && octet <= 255)
                    result += octet;
                else
                    return null;

                if (i + 1 < ipAddressInArray.length)
                    result += ".";
            }
            catch (Throwable e){
                return null;
            }
        }
        return result;
    }
}
