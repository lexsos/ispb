package ispb.base.radius.middleware;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.exception.RadiusException;
import ispb.base.radius.exception.RadiusPacketInvalidLength;
import ispb.base.radius.packet.RadiusPacket;

import java.util.List;

public class RadiusVendorAttributeMiddle implements RadiusMiddleIn {

    public static final int VENDOR_HEADER_LENGTH = 6;

    private final RadiusDictionary dictionary;

    public RadiusVendorAttributeMiddle(RadiusDictionary dictionary){
        this.dictionary = dictionary;
    }

    public RadiusAttribute readVendorAttribute(RadiusAttribute octetAttribute) throws RadiusException {
        if(octetAttribute.getLength() < VENDOR_HEADER_LENGTH)
            throw new RadiusPacketInvalidLength();

        byte[] octets = octetAttribute.getRawValue();

        int vendorId = 0;
        vendorId |= (octets[0] & 0x0ff) << 24;
        vendorId |= (octets[1] & 0x0ff) << 16;
        vendorId |= (octets[2] & 0x0ff) << 8;
        vendorId |= (octets[3] & 0x0ff);

        int vendorType = octets[4] & 0x0ff;
        int length = (octets[5] & 0x0ff) - 2;

        if (length < 0 || (octets.length - VENDOR_HEADER_LENGTH) > length)
            throw new RadiusPacketInvalidLength();

        AttributeType type = dictionary.getType(vendorId, vendorType);
        if (type == null)
            return null;

        RadiusAttribute vendorAttribute = type.newInstance();
        vendorAttribute.readValue(VENDOR_HEADER_LENGTH, length, octets);
        return vendorAttribute;
    }

    public void in(RadiusPacket request, byte[] secret) throws RadiusException {
        List<RadiusAttribute> attributeList = request.getAttributeList();
        for (int i = 0; i < attributeList.size(); i++) {
            RadiusAttribute attribute = attributeList.get(i);
            if (attribute.getType() == RadiusPacket.ATTRIBUTE_VENDOR_SPECIFIC){
                RadiusAttribute vendorAttribute = readVendorAttribute(attribute);
                attributeList.set(i, vendorAttribute);
            }
        }
    }

    public void in(RadiusPacket request, RadiusPacket reply, byte[] secret) throws RadiusException{
        in(reply, secret);
    }

}
