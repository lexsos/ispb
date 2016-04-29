package ispb.radius.servlet;


import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.radius.RadiusServlet;
import ispb.base.service.account.RadiusUserService;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import java.net.InetAddress;

public class DefaultRadiusServlet extends RadiusServlet {

    private final RadiusUserService userService;

    public DefaultRadiusServlet(RadiusUserService userService){
        this.userService = userService;
    }

    protected RadiusPacket access(AccessRequest request, RadiusClientDataSet clientDataSet){

        // TODO Write implementation

        String userName = request.getUserName();
        RadiusUserDataSet user = userService.getUserByName(userName);

        if (user != null) {
            String password = user.getPassword();

            try {
                if (password != null && password.length() > 0 && !request.verifyPassword(password))
                    return makeAccessReject(request);
            } catch (RadiusException e) {
                return makeAccessReject(request);
            }

            RadiusPacket response = makeAccessAccept(request);
            if (user.getIp4Address() != null)
                response.addAttribute("Framed-IP-Address", user.getIp4Address());
            response.addAttribute("Mikrotik-Address-List", "Tariff10M");
            response.addAttribute("Mikrotik-Address-List", "AllowUsers");
            return response;
        }
        else
            return makeAccessReject(request);
    }

}
