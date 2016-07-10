package ispb.base.radius.attribute;


import java.io.InputStream;
import java.io.OutputStream;

public interface RadiusAttribute {

    String getName();
    String getValue();
    void setName(String name);
    void setValue(String value);

    byte[] getRawValue();
    void setRawValue(byte[] value);

    void readValue(int pos, int len, byte[] data);
    void readValue(int len, InputStream in);
    void writeValue(int pos, byte[] data);
    void writeValue(OutputStream out);
    int getValueLen();
}
