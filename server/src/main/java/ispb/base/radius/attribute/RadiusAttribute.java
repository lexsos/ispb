package ispb.base.radius.attribute;


import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.exception.RadiusBadValue;

public interface RadiusAttribute {

    void setAttributeType(AttributeType attributeType);
    AttributeType getAttributeType();

    String getName();
    int getLength();

    int getType();
    void setType(int type);

    String getValue();
    void setValue(String value) throws RadiusBadValue;

    byte[] getRawValue();
    void setRawValue(byte[] value);

    void readValue(int pos, int len, byte[] data);
    int write(int pos, byte[] data);
}
