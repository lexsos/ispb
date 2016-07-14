package ispb.base.radius.auth;


import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.packet.RadiusPacket;

import java.util.Objects;

public class RadiusPapAuth implements RadiusAuth {

    public boolean checkPassword(RadiusPacket packet, String plainPassword){
        for (RadiusAttribute attribute: packet.getAttributeList())
            if (attribute.getType() == RadiusPacket.ATTRIBUTE_USER_PASSWORD)
                return Objects.equals(attribute.getValue(), plainPassword);
        return false;
    }
}
