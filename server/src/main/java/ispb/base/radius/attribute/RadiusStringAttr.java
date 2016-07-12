package ispb.base.radius.attribute;


import ispb.base.radius.exception.RadiusBadValue;

import java.io.UnsupportedEncodingException;

public class RadiusStringAttr extends RadiusOctetAttr {

    public String getValue(){
        if (data == null || data.length == 0)
            return null;

        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return new String(data);
        }
    }

    public void setValue(String value) throws RadiusBadValue {
        if (value == null)
            throw new RadiusBadValue();

        try {
            setRawValue(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            setRawValue(value.getBytes());
        }
    }
}
