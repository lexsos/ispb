package ispb.base.radius.attribute;


import ispb.base.radius.exception.RadiusBadValue;
import ispb.base.utils.Ip4Address;

@SuppressWarnings("WeakerAccess")
public class RadiusIp4Attr extends RadiusOctetAttr {

    public static final int DATA_LENGTH = 4;

    public String getValue()  {
        if (data == null || data.length != DATA_LENGTH)
            return null;

        return Ip4Address.fromBytes(data);
    }

    public void setValue(String value) throws RadiusBadValue {
        byte[] data = Ip4Address.asBytes(value);
        if (data == null)
            throw new RadiusBadValue();

        setRawValue(data);
    }
}
