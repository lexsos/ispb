package ispb.base.radius.io;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.exception.RadiusAttrNotExist;
import ispb.base.radius.exception.RadiusPacketInvalidLength;
import ispb.base.radius.packet.RadiusPacket;

public class RadiusPacketSerializer implements RadiusPacketReader, RadiusPacketWriter {

    private final RadiusDictionary dictionary;

    public RadiusPacketSerializer(RadiusDictionary dictionary){
        this.dictionary = dictionary;
    }

    public RadiusPacket read(byte[] data) throws RadiusPacketInvalidLength, RadiusAttrNotExist {

        if (data.length < RadiusPacket.HEADER_LENGTH)
            throw new RadiusPacketInvalidLength();

        int pos = 0;
        int code = data[pos++] & 0x0ff;
        int identifier = data[pos++] & 0x0ff;
        int length;
        length = (data[pos++] & 0x0ff) << 8;
        length = length | (data[pos++] & 0x0ff);

        if (length != data.length)
            throw new RadiusPacketInvalidLength();

        RadiusPacket packet = new RadiusPacket(code, identifier);
        System.arraycopy(data, pos, packet.getAuthenticator(), 0, RadiusPacket.AUTH_LENGTH);
        pos += RadiusPacket.AUTH_LENGTH;

        while (pos < data.length){

            if (pos >= data.length - 2)
                throw new RadiusPacketInvalidLength();

            int attributeType = data[pos++] & 0x0ff;
            int attributeLength = data[pos++] & 0x0ff;
            int dataLength = attributeLength - 2;

            if (dataLength < 0)
                throw new RadiusPacketInvalidLength();

            if (pos + dataLength > data.length)
                throw new RadiusPacketInvalidLength();

            AttributeType attrType = dictionary.getType(attributeType);

            if (attrType == null)
                throw new RadiusAttrNotExist();

            RadiusAttribute attribute = attrType.newInstance();
            attribute.readValue(pos, dataLength, data);
            packet.addAttribute(attribute);
            pos += dataLength;
        }

        return packet;
    }

    public byte[] write(RadiusPacket radiusPacket){
        int pos = 0;
        int packetLength = radiusPacket.getLength();
        byte[] data = new byte[packetLength];

        data[pos++] = (byte)(radiusPacket.getPacketType() & 0x0ff);
        data[pos++] = (byte)(radiusPacket.getIdentifier() & 0x0ff);

        data[pos++] = (byte)(packetLength >> 8 & 0x0ff);
        data[pos++] = (byte)(packetLength & 0x0ff);

        System.arraycopy(radiusPacket.getAuthenticator(), 0, data, pos, RadiusPacket.AUTH_LENGTH);
        pos += RadiusPacket.AUTH_LENGTH;

        for (RadiusAttribute attribute: radiusPacket.getAttributeList())
            pos += attribute.write(pos, data);

        return data;
    }
}
