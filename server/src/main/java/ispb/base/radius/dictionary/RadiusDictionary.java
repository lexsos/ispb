package ispb.base.radius.dictionary;

import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusAttrAlreadyExist;
import ispb.base.radius.exception.RadiusAttrNotExist;
import ispb.base.radius.exception.RadiusVendorAlreadyExist;
import ispb.base.radius.exception.RadiusVendorNotExist;

import java.util.Set;

public interface RadiusDictionary {

    Set<String> getAttributeNames();

    AttributeType getType(int attributeType);
    AttributeType getType(int vendorId, int attributeType);
    AttributeType getType(String attributeName);

    void addAttribute(String attributeName, int attributeType, Class<? extends RadiusAttribute> attributeClazz)
            throws RadiusAttrAlreadyExist;

    void addValue(String attributeName, String valueName, String value)
            throws RadiusAttrNotExist;

    void addVendor(String vendorName, int vendorId)
            throws RadiusVendorAlreadyExist;

    void addVendorAttribute(String attributeName, int vendorId, int attributeType, Class<? extends RadiusAttribute> clazz)
            throws RadiusAttrAlreadyExist;

    void addVendorAttribute(String attributeName, String vendorName, int attributeType, Class<? extends RadiusAttribute> clazz)
            throws RadiusAttrAlreadyExist, RadiusVendorNotExist;
}

