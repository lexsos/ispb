package ispb.base.utils;


import java.io.UnsupportedEncodingException;

public class HexCodec {

    public static String byteToHex(byte[] data){
        StringBuilder string = new StringBuilder();
        string.append("0x");

        if (data != null)
            for (int i = 0; i < data.length; i++) {
                String digit = Integer.toString(data[i] & 0x0ff, 16);
                if (digit.length() < 2)
                    string.append('0');
                string.append(digit);
            }

        return string.toString();
    }

    public static byte[] hexToByte(String hex){

        if (hex == null || hex.length() < 2 || hex.length() % 2 != 0 || hex.charAt(0) != '0' || hex.charAt(1) != 'x')
            return null;

        int len = hex.length();
        byte[] data = new byte[(len - 2) / 2];
        for (int i = 0; i < data.length; i++) {
            int ia = i * 2 + 2;
            int ib = i * 2 + 3;
            int a = Character.digit(hex.charAt(ia), 16);
            int b = Character.digit(hex.charAt(ib), 16);

            if (a < 0 || a > 15 || b < 0 || b > 15)
                return null;

            data[i] = (byte)( ((a & 0x0f) << 4) + (b & 0x0f));
        }
        return data;
    }

    public static String byteToString(byte[] data){
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return new String(data);
        }
    }

    public static byte[] stringToByte(String str){
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return str.getBytes();
        }
    }
}
