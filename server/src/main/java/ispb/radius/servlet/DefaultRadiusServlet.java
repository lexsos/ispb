package ispb.radius.servlet;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.radius.RadiusAttributeList;
import ispb.base.radius.RadiusAttributeListBuilder;
import ispb.base.radius.RadiusServlet;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.ServiceException;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;


public class DefaultRadiusServlet extends RadiusServlet {

    private final RadiusUserService userService;
    private final CustomerAccountService customerService;
    private final RadiusAttributeListBuilder attributeBuilder;


    public DefaultRadiusServlet(RadiusUserService userService,
                                LogService logService,
                                CustomerAccountService customerService,
                                RadiusAttributeListBuilder attributeBuilder){
        super(logService);
        this.userService = userService;
        this.customerService = customerService;
        this.attributeBuilder = attributeBuilder;
    }

    protected RadiusPacket access(AccessRequest request, RadiusClientDataSet radiusClient){

        String userName = getUserName(request, radiusClient);
        RadiusUserDataSet user = userService.getUserByName(userName);

        if (user != null)
            return userAuth(request, radiusClient, user);
        else
            return unknownUser(request, radiusClient, userName);
    }

    @SuppressWarnings({"WeakerAccess", "UnusedParameters"})
    protected String getUserName(AccessRequest request, RadiusClientDataSet radiusClient){
        return request.getUserName();
    }

    @SuppressWarnings("WeakerAccess")
    protected RadiusPacket unknownUser(AccessRequest request, RadiusClientDataSet radiusClient, String userName){
        if (radiusClient.isAddAuthRequest()){
            RadiusUserDataSet user = new RadiusUserDataSet();
            user.setUserName(userName);
            try {
                userService.create(user);
            }
            catch (ServiceException e){
                getLogService().warn("Can't add new RADIUS auth request for user " + userName, e);
            }
        }
        return makeAccessReject(request);
    }

    @SuppressWarnings("WeakerAccess")
    protected boolean checkPassword(AccessRequest request, RadiusUserDataSet user){
        String password = user.getPassword();
        try {
            if (password != null && password.length() > 0 && !request.verifyPassword(password))
                return false;
        } catch (RadiusException e) {
            getLogService().warn("Can't validate password for RADIUS user " + user.getUserName(), e);
            return false;
        }
        return true;
    }

    @SuppressWarnings("WeakerAccess")
    protected RadiusPacket userAuth(AccessRequest request, RadiusClientDataSet radiusClient, RadiusUserDataSet user){

        CustomerDataSet customer = user.getCustomer();
        if (customer == null)
            return makeAccessReject(request);

        if (!checkPassword(request, user))
            return makeAccessReject(request);

        CustomerSummeryView summery = customerService.getSummeryById(customer.getId());
        if (summery.getStatus() == CustomerStatus.INACTIVE && radiusClient.isRejectInactive())
            return makeAccessReject(request);

        RadiusPacket response = makeAccessAccept(request);

        RadiusAttributeList attrList = attributeBuilder.getAttributeList(user);

        if (user.getIp4Address() != null)
            attrList.addAttribute("Framed-IP-Address", user.getIp4Address());

        addAttributes(response, attrList);

        return response;
    }
}
