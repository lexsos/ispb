package ispb.base.radius.attribute;


import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.exception.RadiusBadValue;

public interface RadiusAttribute {

    void setDictionary(RadiusDictionary dictionary);

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
