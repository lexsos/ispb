package ispb.base.utils;

public class Ip4Address {

    public static byte[] asBytes(String ipAddress){

        if (ipAddress == null)
            return null;

        String[] ipAddressInArray = ipAddress.trim().split("\\.");

        if (ipAddressInArray.length != 4)
            return null;

        byte[] result = new byte[4];

        for (int i=0; i<ipAddressInArray.length; i++){
            try {
                int octet = Integer.parseInt(ipAddressInArray[i]);
                if (0 <= octet && octet <= 255)
                    result[i] = (byte)(octet & 0x0ff);
                else
                    return null;
            }
            catch (Throwable e){
                return null;
            }
        }
        return result;
    }


    public static String fromBytes(byte[] data){
        if (data == null || data.length != 4)
            return null;

        String result = "";
        for (int i=0; i<data.length; i++){
            result += Integer.toString(data[i]);
            if (i + 1 < data.length)
                result += ".";
        }
        return result;
    }


    public static String normalize(String ipAddress){

        if (ipAddress == null)
            return null;

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
