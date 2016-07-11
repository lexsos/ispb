package ispb.base.radius.attribute;


public interface RadiusAttribute {

    String getName();
    int getType();

    String getValue();
    void setValue(String value);

    byte[] getRawValue();
    void setRawValue(byte[] value);

    void readValue(int pos, int len, byte[] data);
    int write(int pos, byte[] data);
}
