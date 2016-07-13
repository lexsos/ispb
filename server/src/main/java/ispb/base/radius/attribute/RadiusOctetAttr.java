package ispb.base.radius.attribute;

import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.exception.RadiusBadValue;
import ispb.base.utils.HexCodec;


@SuppressWarnings("WeakerAccess")
public class RadiusOctetAttr implements RadiusAttribute {

    public final static int HEADER_LENGTH = 2;

    protected byte[] data;
    protected int type; // TODO: refactoring - get from attributeType
    protected AttributeType attributeType;

    public void setAttributeType(AttributeType attributeType){
        this.attributeType = attributeType;
    }

    public AttributeType getAttributeType(){
        return attributeType;
    }

    public String getName(){
        return attributeType.getAttributeName();
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getLength(){
        if (data == null)
            return HEADER_LENGTH;
        return HEADER_LENGTH + data.length;
    }

    public String getValue(){
        return HexCodec.byteToHex(data);
    }

    public void setValue(String value) throws RadiusBadValue {
        data = HexCodec.hexToByte(value);
    }

    public byte[] getRawValue(){
        return data;
    }

    public void setRawValue(byte[] value){
        data = value;
    }

    public void readValue(int pos, int len, byte[] data){
        if (this.data == null || this.data.length != len)
            this.data = new byte[len];

        System.arraycopy(data, pos, this.data, 0, len);
    }

    public int write(int pos, byte[] data){
        data[pos] = (byte)(type & 0x0ff);
        data[pos + 1] = (byte)(getLength() & 0x0ff);

        if (this.data != null)
            System.arraycopy(this.data, 0, data, pos + 2, this.data.length);

        return getLength();
    }
}
