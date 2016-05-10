/**
 * $Id: MemoryDictionary.java,v 1.2 2006/09/24 10:06:38 wuttke Exp $
 * Created on 28.08.2005
 * 
 * @author mw
 * @version $Revision: 1.2 $
 */
package org.tinyradius.dictionary;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A dictionary that keeps the values and names in hash maps
 * in the memory. The dictionary has to be filled using the
 * methods <code>addAttributeType</code> and <code>addVendor</code>.
 * 
 * @see #addAttributeType(AttributeType)
 * @see #addVendor(int, String)
 * @see RadiusDictionary
 * @see WritableRadiusDictionary
 */
public class MemoryDictionary implements WritableRadiusDictionary {

	/**
	 * Returns the AttributeType for the vendor -1 from the
	 * cache.
	 * 
	 * @param typeCode
	 *            attribute type code
	 * @return AttributeType or null
	 * @see RadiusDictionary#getAttributeTypeByCode(int)
	 */
	public AttributeType getAttributeTypeByCode(int typeCode) {
		return getAttributeTypeByCode(-1, typeCode);
	}

	/**
	 * Returns the specified AttributeType object.
	 * 
	 * @param vendorCode
	 *            vendor ID or -1 for "no vendor"
	 * @param typeCode
	 *            attribute type code
	 * @return AttributeType or null
	 * @see RadiusDictionary#getAttributeTypeByCode(int, int)
	 */
	public AttributeType getAttributeTypeByCode(int vendorCode, int typeCode) {
		Map<Integer, AttributeType> vendorAttributes = attributesByCode.get(vendorCode);
		if (vendorAttributes == null) {
			return null;
		}
		return vendorAttributes.get(typeCode);
	}

	/**
	 * Retrieves the attribute type with the given name.
	 * 
	 * @param typeName
	 *            name of the attribute type
	 * @return AttributeType or null
	 * @see RadiusDictionary#getAttributeTypeByName(java.lang.String)
	 */
	public AttributeType getAttributeTypeByName(String typeName) {
		return attributesByName.get(typeName);
	}

	/**
	 * Searches the vendor with the given name and returns its
	 * code. This method is seldomly used.
	 * 
	 * @param vendorName
	 *            vendor name
	 * @return vendor code or -1
	 * @see RadiusDictionary#getVendorId(java.lang.String)
	 */
	public int getVendorId(String vendorName) {

		for (Map.Entry<Integer, String> e: vendorsByCode.entrySet()) {
			if (e.getValue().equals(vendorName))
				return e.getKey();
		}
		return -1;
	}

	/**
	 * Retrieves the name of the vendor with the given code from
	 * the cache.
	 * 
	 * @param vendorId
	 *            vendor number
	 * @return vendor name or null
	 * @see RadiusDictionary#getVendorName(int)
	 */
	public String getVendorName(int vendorId) {
		return vendorsByCode.get(vendorId);
	}

	/**
	 * Adds the given vendor to the cache.
	 * 
	 * @param vendorId
	 *            vendor ID
	 * @param vendorName
	 *            name of the vendor
	 * @exception IllegalArgumentException
	 *                empty vendor name, invalid vendor ID
	 */
	public void addVendor(int vendorId, String vendorName) {
		if (vendorId < 0)
			throw new IllegalArgumentException("vendor ID must be positive");
		if (getVendorName(vendorId) != null)
			throw new IllegalArgumentException("duplicate vendor code");
		if (vendorName == null || vendorName.length() == 0)
			throw new IllegalArgumentException("vendor name empty");
		vendorsByCode.put(vendorId, vendorName);
	}

	/**
	 * Adds an AttributeType object to the cache.
	 * 
	 * @param attributeType
	 *            AttributeType object
	 * @exception IllegalArgumentException
	 *                duplicate attribute name/type code
	 */
	public void addAttributeType(AttributeType attributeType) {
		if (attributeType == null)
			throw new IllegalArgumentException("attribute type must not be null");

		int vendorId =  attributeType.getVendorId();
		int typeCode =  attributeType.getTypeCode();
		String attributeName = attributeType.getName();

		if (attributesByName.containsKey(attributeName))
			throw new IllegalArgumentException("duplicate attribute name: " + attributeName);

		Map<Integer, AttributeType> vendorAttributes = attributesByCode.get(vendorId);
		if (vendorAttributes == null) {
			vendorAttributes = new ConcurrentHashMap<>();
			attributesByCode.put(vendorId, vendorAttributes);
		}
		if (vendorAttributes.containsKey(typeCode))
			throw new IllegalArgumentException("duplicate type code: " + typeCode);

		attributesByName.put(attributeName, attributeType);
		vendorAttributes.put(typeCode, attributeType);

		attributeNames.add(attributeName);
	}

	public Set<String> getAttributeNamesSet(){
		return attributeNames;
	}

	private final Map<Integer, String> vendorsByCode = new ConcurrentHashMap<>(); // <Integer, String>
	private final Map<Integer, Map<Integer, AttributeType>> attributesByCode = new ConcurrentHashMap<>(); // <Integer, <Integer, AttributeType>>
	private final Map<String, AttributeType> attributesByName = new ConcurrentHashMap<>(); // <String, AttributeType>
	private final Set<String> attributeNames = new ConcurrentSkipListSet<>();

}
