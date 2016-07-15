package ispb.radius.server;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.RadiusUserDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.radius.packet.RadiusAttributeList;
import ispb.base.radius.server.RadiusAttributeListBuilder;
import ispb.base.service.account.CustomerAccountService;
import ispb.base.service.account.RadiusUserService;
import ispb.base.service.dictionary.TariffDictionaryService;

public class RadiusAttributeListBuilderImpl implements RadiusAttributeListBuilder {

    private final CustomerAccountService customerService;
    private final TariffDictionaryService tariffService;
    private final RadiusUserService userService;

    public RadiusAttributeListBuilderImpl(
            CustomerAccountService customerService,
            TariffDictionaryService tariffService,
            RadiusUserService userService){
        this.customerService = customerService;
        this.tariffService = tariffService;
        this.userService = userService;
    }

    public RadiusAttributeList getAttributeList(RadiusUserDataSet user){
        RadiusAttributeList attrList = new RadiusAttributeList();

        CustomerDataSet customer = user.getCustomer();
        if (customer == null)
            return attrList;
        CustomerSummeryView summery = customerService.getSummeryById(customer.getId());

        attrList.addAttributeList(tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ALWAYS));
        if (summery.getStatus() == CustomerStatus.ACTIVE)
            attrList.addAttributeList(tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ON_ACTIVE));
        else
            attrList.addAttributeList(tariffService.getAttributeList(summery.getTariff().getId(), RadiusAttributeCondition.ON_INACTIVE));

        attrList.addAttributeList(userService.getAttributeList(user.getId(), RadiusAttributeCondition.ALWAYS));
        if (summery.getStatus() == CustomerStatus.ACTIVE)
            attrList.addAttributeList(userService.getAttributeList(user.getId(), RadiusAttributeCondition.ON_ACTIVE));
        else
            attrList.addAttributeList(userService.getAttributeList(user.getId(), RadiusAttributeCondition.ON_INACTIVE));

        return attrList;
    }
}
