package ispb.base.radius.dictionary;

import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusAttrAlreadyExist;
import ispb.base.radius.exception.RadiusAttrNotExist;
import ispb.base.radius.exception.RadiusVendorAlreadyExist;
import ispb.base.radius.exception.RadiusVendorNotExist;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RadiusMemoryDictionary implements RadiusDictionary {

    private final Map<String, AttributeType> attributeTypeByName; // <AttributeName, AttributeType>

    private final Map<Integer, AttributeType> stdAttributes; // <StandardAttributeTypeId, AttributeType>
    private final Map<Integer, Map<Integer, AttributeType>> vendorAttributes; // <VendorId, <VendorTypeId, AttributeType>>
    private final Map<String, Integer> vendors; // <VendorName, VendorId>

    public RadiusMemoryDictionary(){
        attributeTypeByName = new ConcurrentHashMap<>();
        stdAttributes = new ConcurrentHashMap<>();
        vendorAttributes = new ConcurrentHashMap<>();
        vendors = new ConcurrentHashMap<>();
    }

    public Set<String> getAttributeNames(){
        return attributeTypeByName.keySet();
    }

    public AttributeType getType(int attributeType){
        return stdAttributes.get(attributeType);
    }

    public AttributeType getType(int vendorId, int attributeType){
        if (vendorAttributes.containsKey(vendorId))
            return vendorAttributes.get(vendorId).get(attributeType);
        return null;
    }

    public AttributeType getType(String attributeName){
        return attributeTypeByName.get(attributeName);
    }

    public void addAttribute(String attributeName, int attributeType, Class<? extends RadiusAttribute> attributeClazz)
            throws RadiusAttrAlreadyExist{

        if (attributeTypeByName.containsKey(attributeName) || stdAttributes.containsKey(attributeType))
            throw new RadiusAttrAlreadyExist();

        AttributeType type =  new AttributeType(attributeName, attributeType, attributeClazz);
        attributeTypeByName.put(attributeName, type);
        stdAttributes.put(attributeType, type);
    }

    public void addValue(String attributeName, String valueName, String value) throws RadiusAttrNotExist{
        if (!attributeTypeByName.containsKey(attributeName))
            throw new RadiusAttrNotExist();

        attributeTypeByName.get(attributeName).addValue(valueName, value);
    }

    public void addVendor(String vendorName, int vendorId) throws RadiusVendorAlreadyExist{
        if (vendors.containsKey(vendorName))
            throw new RadiusVendorAlreadyExist();

        vendors.put(vendorName, vendorId);
    }

    public void addVendorAttribute(String attributeName, int vendorId, int attributeType, Class<? extends RadiusAttribute> clazz)
            throws RadiusAttrAlreadyExist{

        if (attributeTypeByName.containsKey(attributeName))
            throw new RadiusAttrAlreadyExist();
        if (vendorAttributes.containsKey(vendorId) && vendorAttributes.get(vendorId).containsKey(attributeType))
            throw new RadiusAttrAlreadyExist();

        VendorAttributeType type = new VendorAttributeType(attributeName, vendorId, attributeType, clazz);
        attributeTypeByName.put(attributeName, type);
        if (!vendorAttributes.containsKey(vendorId))
            vendorAttributes.put(vendorId, new ConcurrentHashMap<>());
        vendorAttributes.get(vendorId).put(attributeType, type);
    }

    public void addVendorAttribute(String attributeName, String vendorName, int attributeType, Class<? extends RadiusAttribute> clazz)
            throws RadiusAttrAlreadyExist, RadiusVendorNotExist{
        if (!vendors.containsKey(vendorName))
            throw new RadiusVendorNotExist();
        addVendorAttribute(attributeName, vendors.get(vendorName), attributeType, clazz);
    }
}