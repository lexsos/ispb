package ispb.base.radius;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.ServiceException;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RadiusServlet {

    private final LogService logService;
    private final RadiusAttributeListBuilder attributeBuilder;
    private final RadiusUserService userService;
    private final CustomerAccountService customerService;

    private final boolean addSessionTimeout;
    private final String defaultSessionTimeout;
    private final String sessionTimeoutAttribute;

    private final boolean addIpv4Addr;
    private final String ipv4AddrAttribute;

    public RadiusServlet(
            LogService logService,
            RadiusAttributeListBuilder attributeBuilder,
            RadiusUserService userService,
            CustomerAccountService customerService,
            Config config){
        this.logService = logService;
        this.attributeBuilder = attributeBuilder;
        this.userService = userService;
        this.customerService = customerService;

        addSessionTimeout = config.getAsBool("radius.addSessionTimeout");
        defaultSessionTimeout = config.getAsStr("radius.defaultSessionTimeout");
        sessionTimeoutAttribute = config.getAsStr("radius.sessionTimeoutAttribute");

        addIpv4Addr = config.getAsBool("radius.addIpv4Addr");
        ipv4AddrAttribute= config.getAsStr("radius.ipv4AddrAttribute");
    }

    public RadiusPacket service(RadiusPacket request, RadiusClientDataSet clientDataSet){

        if (request instanceof AccessRequest)
            return access((AccessRequest)request, clientDataSet);
        else if (request instanceof AccountingRequest)
            return accounting((AccountingRequest)request, clientDataSet);

        return null;
    }

    protected RadiusPacket access(AccessRequest request, RadiusClientDataSet radiusClient){

        String userName = getUserName(request, radiusClient);
        RadiusUserDataSet user = getUserService().getUserByName(userName);

        if (user != null)
            return userAuth(request, radiusClient, user);
        else
            return unknownUser(request, radiusClient, userName);
    }

    protected RadiusPacket unknownUser(AccessRequest request, RadiusClientDataSet radiusClient, String userName){
        if (radiusClient.isAddAuthRequest()){
            RadiusUserDataSet user = new RadiusUserDataSet();
            user.setUserName(userName);
            try {
                getUserService().create(user);
            }
            catch (ServiceException e){
                getLogService().warn("Can't add new RADIUS auth request for user " + userName, e);
            }
        }
        return makeAccessReject(request);
    }

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
        addAttributes(response, makeAttributeList(user));

        return response;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedParameters"})
    protected String getUserName(AccessRequest request, RadiusClientDataSet radiusClient){
        return request.getUserName();
    }

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    protected LogService getLogService(){
        return logService;
    }

    protected RadiusUserService getUserService(){
        return userService;
    }

    protected RadiusAttributeListBuilder getAttributeBuilder(){
        return attributeBuilder;
    }

    @SuppressWarnings({"WeakerAccess", "SameReturnValue", "UnusedParameters"})
    protected RadiusPacket accounting(AccountingRequest request, RadiusClientDataSet clientDataSet){
        return null;
    }

    protected RadiusPacket makeAccessAccept(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_ACCEPT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccessReject(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCESS_REJECT, request.getPacketIdentifier());
    }

    protected RadiusPacket makeAccountingResponse(RadiusPacket request){
        return new RadiusPacket(RadiusPacket.ACCOUNTING_RESPONSE, request.getPacketIdentifier());
    }

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

    protected void addAttributes(RadiusPacket radiusPacket, Iterable<? extends RadiusAttribute> attributeList){
        for (RadiusAttribute attribute: attributeList)
            try {
                radiusPacket.addAttribute(attribute.getAttributeName(), attribute.getAttributeValue());
            }
            catch (Throwable e){
                getLogService().warn("Error while add RADIUS attribute " + attribute.getAttributeName() + " to RADIUS packet", e);
            }
    }

    protected RadiusAttributeList makeAttributeList(RadiusUserDataSet user){

        RadiusAttributeList attrList = getAttributeBuilder().getAttributeList(user);

        if (addIpv4Addr && user.getIp4Address() != null)
            attrList.addAttribute(ipv4AddrAttribute, user.getIp4Address());

        if (addSessionTimeout && !attrList.containsAttribute(sessionTimeoutAttribute))
            attrList.addAttribute(sessionTimeoutAttribute, defaultSessionTimeout);

        return attrList;
    }
}
