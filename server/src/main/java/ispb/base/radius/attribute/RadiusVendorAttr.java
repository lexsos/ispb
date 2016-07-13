package ispb.base.radius.attribute;


import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.exception.RadiusBadValue;

public class RadiusVendorAttr implements RadiusAttribute {

    public static final int VENDOR_SPECIFIC_TYPE = 26;
    public static final int VENDOR_HEADER_LENGTH = 6;

    private final int vendorId;
    private final RadiusAttribute innerAttr;
    private AttributeType attributeType;

    public RadiusVendorAttr(int vendorId, RadiusAttribute innerAttr){
        this.vendorId = vendorId;
        this.innerAttr = innerAttr;
    }

    public void setAttributeType(AttributeType attributeType){
        this.attributeType = attributeType;
    }

    public AttributeType getAttributeType(){
        return attributeType;
    }

    public String getName(){
        return attributeType.getAttributeName();
    }

    public int getLength(){
        return VENDOR_HEADER_LENGTH + innerAttr.getLength();
    }

    public int getType(){
        return VENDOR_SPECIFIC_TYPE;
    }

    public void setType(int type){
    }

    public String getValue(){
        return innerAttr.getValue();
    }

    public void setValue(String value) throws RadiusBadValue{
        innerAttr.setValue(value);
    }

    public byte[] getRawValue(){
        return innerAttr.getRawValue();
    }

    public void setRawValue(byte[] value){
        innerAttr.setRawValue(value);
    }

    public void readValue(int pos, int len, byte[] data){
        innerAttr.readValue(pos, len, data);
    }

    public int write(int pos, byte[] data){
        data[pos++] = (byte)(VENDOR_SPECIFIC_TYPE & 0x0ff);
        data[pos++] = (byte)(getLength() & 0x0ff);

        data[pos++] = (byte)(vendorId >> 24 & 0x0ff);
        data[pos++] = (byte)(vendorId >> 16 & 0x0ff);
        data[pos++] = (byte)(vendorId >> 8 & 0x0ff);
        data[pos++] = (byte)(vendorId & 0x0ff);

        innerAttr.write(pos, data);

        return getLength();
    }

}
