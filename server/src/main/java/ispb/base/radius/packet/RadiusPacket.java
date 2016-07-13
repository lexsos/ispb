package ispb.base.radius.packet;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.exception.RadiusBadAuthenticator;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class RadiusPacket {

    public static final int HEADER_LENGTH = 20;
    public static final int MAX_LENGTH = 4096;
    public static final int AUTH_LENGTH = 16;

    public static final int ACCESS_REQUEST = 1;
    public static final int ACCESS_ACCEPT = 2;
    public static final int ACCESS_REJECT = 3;
    public static final int ACCOUNTING_REQUEST = 4;
    public static final int ACCOUNTING_RESPONSE = 5;
    public static final int ACCOUNTING_STATUS = 6;
    public static final int PASSWORD_REQUEST = 7;
    public static final int PASSWORD_ACCEPT = 8;
    public static final int PASSWORD_REJECT = 9;
    public static final int ACCOUNTING_MESSAGE = 10;
    public static final int ACCESS_CHALLENGE = 11;
    public static final int STATUS_SERVER = 12;
    public static final int STATUS_CLIENT = 13;
    public static final int DISCONNECT_REQUEST = 40; // RFC 2882
    public static final int DISCONNECT_ACK = 41;
    public static final int DISCONNECT_NAK = 42;
    public static final int COA_REQUEST = 43;
    public static final int COA_ACK = 44;
    public static final int COA_NAK = 45;
    public static final int STATUS_REQUEST = 46;
    public static final int STATUS_ACCEPT = 47;
    public static final int STATUS_REJECT = 48;
    public static final int RESERVED = 255;

    private int packetType;
    private int identifier;
    private final byte[] authenticator = new byte[AUTH_LENGTH];
    private List<RadiusAttribute> attributeList = new  LinkedList<>();


    public RadiusPacket(){
        packetType = 0;
        identifier = 0;
    }

    public RadiusPacket(int type, int identifier){
        packetType = type;
        this.identifier = identifier;
    }

    public void setAuth(byte[] auth) throws RadiusBadAuthenticator{
        if (auth == null || auth.length != AUTH_LENGTH)
            throw new RadiusBadAuthenticator();

        System.arraycopy(auth, 0, authenticator, 0, authenticator.length);
    }

    public byte[] getAuthenticator(){
        return authenticator;
    }

    public int getPacketType(){
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getIdentifier(){
        return identifier;
    }

    public void setIdentifier(int identifier){
        this.identifier = identifier;
    }

    public int getLength(){
        int length = HEADER_LENGTH;
        for (RadiusAttribute attr: attributeList)
            length += attr.getLength();
        return length;
    }

    public void addAttribute(RadiusAttribute attribute){
        attributeList.add(attribute);
    }

    public List<RadiusAttribute> getAttributeList(){
        return attributeList;
    }

    public void setAttributeList(List<RadiusAttribute> attributeList){
        this.attributeList = attributeList;
    }
}
