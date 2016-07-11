package ispb.base.radius.attribute;


import ispb.base.radius.exception.RadiusBadValue;
import ispb.base.utils.Ip4Address;

public class RadiusIp4Attr extends RadiusOctetAttr {

    public static final int DATA_LENGTH = 4;

    public RadiusIp4Attr(int type){
        super(type);
    }

    public String getValue() throws RadiusBadValue {
        if (data == null || data.length != DATA_LENGTH)
            throw new RadiusBadValue();

        String result = Ip4Address.fromBytes(data);
        if (result == null)
            throw new RadiusBadValue();

        return result;
    }

    public void setValue(String value) throws RadiusBadValue {
        byte[] data = Ip4Address.asBytes(value);
        if (data == null)
            throw new RadiusBadValue();

        setRawValue(data);
    }
}
