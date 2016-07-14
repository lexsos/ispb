package ispb.radius.servlet;

import ispb.base.radius.RadiusAttributeListBuilder;
import ispb.base.radius.server.RadiusServlet;
import ispb.base.resources.Config;
import ispb.base.service.LogService;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;


public class DefaultRadiusServlet extends RadiusServlet {



    public DefaultRadiusServlet(LogService logService,
                                RadiusAttributeListBuilder attributeBuilder,
                                RadiusUserService userService,
                                CustomerAccountService customerService,
                                Config config){
        super(logService, attributeBuilder, userService, customerService, config);
    }
}
