package ispb.base.radius.attribute;


import ispb.base.radius.exception.RadiusBadValue;

@SuppressWarnings("WeakerAccess")
public class RadiusIntegerAttr extends RadiusOctetAttr {

    public static final int DATA_LENGTH = 4;

    public String getValue(){
        if (data == null || data.length != DATA_LENGTH)
            return null;

        long i = 0;
        i |= (long)(data[0] & 0x0ff) << 24;
        i |= (long)(data[1] & 0x0ff) << 16;
        i |= (long)(data[2] & 0x0ff) << 8;
        i |= (long)(data[3] & 0x0ff);
        return Long.toString(i);
    }

    public void setValue(String value) throws RadiusBadValue {
        if (data == null || data.length != DATA_LENGTH)
            data = new byte[DATA_LENGTH];

        try {
            long i = Long.parseLong(value);
            data[0] = (byte)(i >> 24 & 0x0ff);
            data[1] = (byte)(i >> 16 & 0x0ff);
            data[2] = (byte)(i >> 8 & 0x0ff);
            data[3] = (byte)(i & 0x0ff);
        }
        catch (NumberFormatException e){
            throw new RadiusBadValue();
        }
    }
}
