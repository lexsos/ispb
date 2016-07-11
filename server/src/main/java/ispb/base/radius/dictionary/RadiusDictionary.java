package ispb.base.radius.dictionary;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.attribute.RadiusVendorAttribute;

import java.util.Set;

public interface RadiusDictionary {

    RadiusAttribute createByType(int type);
    RadiusAttribute createByVendorType(int vendorId, int vendorType);
    RadiusAttribute createByName(String name);

    String valueToName(String attributeName, String value);
    String nameToValue(String attributeName, String name);
    Set<String> getAttributeValues(String attributeName);

    String getAttributeName(int type);
    String getVendorAttributeName(int vendorId, int vendorType);
    int getVendorId(String vendorName);
    String getVendorName(int vendorId);

    Set<String> getAttributeNames();

    void addAttribute(String name, int type, Class<? extends RadiusAttribute> clazz);
    void addValue(String name, String value);
    void addVendor(String name, int vendorId);
    void addVendorAttribute(String name, int vendorId, int type, Class<? extends RadiusVendorAttribute> clazz);
}

