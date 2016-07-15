package ispb.base.radius.server;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusClientDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.attribute.RadiusAttribute;
import ispb.base.radius.attribute.RadiusAttributeContainer;
import ispb.base.radius.auth.RadiusAuthProcessor;
import ispb.base.radius.dictionary.AttributeType;
import ispb.base.radius.dictionary.RadiusDictionary;
import ispb.base.radius.exception.RadiusBadValue;
import ispb.base.radius.packet.RadiusPacket;
import ispb.base.radius.packet.RadiusPacketBuilder;
import ispb.base.resources.Config;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.exception.ServiceException;


public class RadiusServlet {

    private final boolean addSessionTimeout;
    private final String defaultSessionTimeout;
    private final String sessionTimeoutAttribute;

    private final boolean addIpv4Addr;
    private final String ipv4AddrAttribute;

    public RadiusServlet(Config config){

        addSessionTimeout = config.getAsBool("radius.addSessionTimeout");
        defaultSessionTimeout = config.getAsStr("radius.defaultSessionTimeout");
        sessionTimeoutAttribute = config.getAsStr("radius.sessionTimeoutAttribute");

        addIpv4Addr = config.getAsBool("radius.addIpv4Addr");
        ipv4AddrAttribute= config.getAsStr("radius.ipv4AddrAttribute");
    }

    public String getSharedSecret(RadiusClientDataSet clientDataSet){
        return clientDataSet.getSecret();
    }

    public RadiusPacket service(RadiusServletContext context){
        RadiusPacket request = context.getRequest();

        if (request.getPacketType() == RadiusPacket.ACCESS_REQUEST)
            return access(context);
        else if (request.getPacketType() == RadiusPacket.ACCOUNTING_REQUEST)
            return accounting(context);

        return null;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    protected RadiusPacket accounting(RadiusServletContext context){
        return null;
    }

    protected RadiusPacket access(RadiusServletContext context){
        String userName = getUserName(context);
        if (userName == null)
            return null;

        RadiusUserDataSet user = getUserService(context).getUserByName(userName);
        if (user != null)
            return userAuth(context, user);
        else
            return unknownUser(context, userName);
    }

    protected String getUserName(RadiusServletContext context){
        return context.getAttributeList().getFirstValue("User-Name");
    }

    protected RadiusUserService getUserService(RadiusServletContext context){
        return context.getApplication().getByType(RadiusUserService.class);
    }

    protected CustomerAccountService getCustomerService(RadiusServletContext context){
        return context.getApplication().getByType(CustomerAccountService.class);
    }

    protected RadiusPacket userAuth(RadiusServletContext context, RadiusUserDataSet user){
        CustomerDataSet customer = user.getCustomer();
        if (customer == null)
            return RadiusPacketBuilder.getAccessReject(context.getRequest());

        if (!checkPassword(context, user))
            return RadiusPacketBuilder.getAccessReject(context.getRequest());

        CustomerSummeryView summery = getCustomerService(context).getSummeryById(customer.getId());
        if (summery.getStatus() == CustomerStatus.INACTIVE && context.getClient().isRejectInactive())
            return RadiusPacketBuilder.getAccessReject(context.getRequest());

        RadiusPacket response = RadiusPacketBuilder.getAccessAccept(context.getRequest());
        addAttributes(response, user, context);
        return response;
    }

    protected RadiusPacket unknownUser(RadiusServletContext context, String userName){
        if (context.getClient().isAddAuthRequest()){
            RadiusUserDataSet user = new RadiusUserDataSet();
            user.setUserName(userName);
            try {
                getUserService(context).create(user);
            }
            catch (ServiceException e){
                context.getLogService().warn("Can't add new RADIUS auth request for user " + userName, e);
            }
        }
        return RadiusPacketBuilder.getAccessReject(context.getRequest());
    }

    private RadiusAuthProcessor getAuth(RadiusServletContext context){
        return context.getApplication().getByType(RadiusAuthProcessor.class);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    protected boolean checkPassword(RadiusServletContext context, RadiusUserDataSet user){
        String password = user.getPassword();
        RadiusAuthProcessor auth = getAuth(context);

        if (password == null || password.length() == 0)
            return true;
        return auth.checkPassword(context.getRequest(), password);
    }

    protected RadiusAttributeListBuilder getAttributeBuilder(RadiusServletContext context){
        return context.getApplication().getByType(RadiusAttributeListBuilder.class);
    }

    protected RadiusDictionary getDictionary(RadiusServletContext context){
        return context.getApplication().getByType(RadiusDictionary.class);
    }

    protected void addAttributes(RadiusPacket response, RadiusUserDataSet user, RadiusServletContext context){
        RadiusAttributeList attrList = getAttributeBuilder(context).getAttributeList(user);
        RadiusDictionary dictionary = getDictionary(context);

        if (addIpv4Addr && user.getIp4Address() != null)
            attrList.addAttribute(ipv4AddrAttribute, user.getIp4Address());

        if (addSessionTimeout && !attrList.containsAttribute(sessionTimeoutAttribute))
            attrList.addAttribute(sessionTimeoutAttribute, defaultSessionTimeout);

        for (RadiusAttributeContainer container: attrList) {
            AttributeType type = dictionary.getType(container.getAttributeName());

            if (type == null) {
                context.getLogService().warn("RADIUS unknown attribute " + container.getAttributeName());
                continue;
            }

            RadiusAttribute attribute = type.newInstance();
            try {
                attribute.setValue(container.getAttributeValue());
            }
            catch (RadiusBadValue e){
                context.getLogService().warn("RADIUS bad value: " + container.getAttributeValue() + " for attribute: " + container.getAttributeName());
                continue;
            }
            response.addAttribute(attribute);
        }
    }

}
