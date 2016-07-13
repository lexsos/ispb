package ispb.base.radius.dictionary;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.attribute.RadiusVendorAttr;

public class VendorAttributeType extends AttributeType {

    private final int vendorId;

    public VendorAttributeType(String attributeName, int vendorId, int vendorType, Class<? extends RadiusAttribute> clazz){
        super(attributeName, vendorType, clazz);
        this.vendorId = vendorId;
    }

    private int getVendorId(){
        return vendorId;
    }

    public RadiusAttribute newInstance(){
        try {
            RadiusAttribute innerAttribute = clazz.newInstance();
            innerAttribute.setType(type);
            innerAttribute.setAttributeType(this);

            RadiusVendorAttr vendorAttr = new RadiusVendorAttr(getVendorId(), innerAttribute);
            vendorAttr.setAttributeType(this);
            return vendorAttr;
        }catch (Throwable throwable){
            return null;
        }
    }
}
