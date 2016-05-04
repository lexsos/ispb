package ispb.radius.servlet;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.radius.RadiusAttribute;
import ispb.base.radius.RadiusServlet;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.dictionary.TariffDictionaryService;
import ispb.base.service.exception.ServiceException;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import java.util.List;


public class DefaultRadiusServlet extends RadiusServlet {

    private final RadiusUserService userService;
    private final CustomerAccountService customerService;
    private final TariffDictionaryService tariffService;


    public DefaultRadiusServlet(RadiusUserService userService,
                                LogService logService,
                                CustomerAccountService customerService,
                                TariffDictionaryService tariffService){
        super(logService);
        this.userService = userService;
        this.customerService = customerService;
        this.tariffService = tariffService;
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
        if (user.getIp4Address() != null)
            response.addAttribute("Framed-IP-Address", user.getIp4Address());

        List<? extends RadiusAttribute> attributeList;

        attributeList = tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ALWAYS);
        addAttributes(response, attributeList);

        if (summery.getStatus() == CustomerStatus.ACTIVE)
            attributeList = tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ON_ACTIVE);
        else
            attributeList = tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ON_INACTIVE);
        addAttributes(response, attributeList);


        attributeList = userService.getAttributeList(user.getId(), RadiusAttributeCondition.ALWAYS);
        addAttributes(response, attributeList);

        if (summery.getStatus() == CustomerStatus.ACTIVE)
            attributeList = userService.getAttributeList(user.getId(), RadiusAttributeCondition.ON_ACTIVE);
        else
            attributeList = userService.getAttributeList(user.getId(), RadiusAttributeCondition.ON_INACTIVE);
        addAttributes(response, attributeList);

        return response;
    }
}
