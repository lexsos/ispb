package ispb.base.radius.auth;


import ispb.base.radius.packet.RadiusPacket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RadiusAuthProcessor implements RadiusAuth {

    private final List<RadiusAuth> authMethodList = new CopyOnWriteArrayList<>();

    public void addAuthMethod(RadiusAuth authMethod){
        authMethodList.add(authMethod);
    }

    public boolean checkPassword(RadiusPacket packet, String plainPassword){
        for (RadiusAuth auth: authMethodList)
            if (auth.checkPassword(packet, plainPassword))
                return true;
        return false;
    }
}
