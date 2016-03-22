package ispb.account;


import ispb.base.Application;
import ispb.base.db.dataset.*;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.account.*;
import ispb.base.utils.DateUtils;
import ispb.base.utils.TextMessages;

import java.util.Date;

public class TariffPolicyServiceImpl implements TariffPolicyService {

    private final Application application;

    public TariffPolicyServiceImpl(Application application){
        this.application = application;
    }

    public void paymentApplied(PaymentDataSet payment){

        CustomerStatusService statusService = getCustomerStatusService();
        TariffAssignmentService tariffService = getTariffService();
        PaymentService paymentService = getPaymentService();

        CustomerDataSet customer = payment.getCustomer();
        Date paymentDate = payment.getApplyAt();
        CustomerStatusDataSet statusDataSet = statusService.getStatus(customer, paymentDate);
        TariffDataSet currentTariff = tariffService.getTariff(customer, paymentDate);

        if (statusDataSet == null || currentTariff == null)
            return;

        double balance = paymentService.getBalance(customer, paymentDate);
        double balanceThreshold = currentTariff.getOffThreshold();
        CustomerStatus status = statusDataSet.getStatus();
        CustomerStatusCause cause = statusDataSet.getCause();

        if (status == CustomerStatus.ACTIVE && balance < balanceThreshold)
            statusService.setStatus(customer, CustomerStatus.INACTIVE, CustomerStatusCause.SYSTEM_FINANCE, paymentDate);
        else if(status == CustomerStatus.INACTIVE && cause == CustomerStatusCause.SYSTEM_FINANCE && balance >= balanceThreshold)
            statusService.setStatus(customer, CustomerStatus.ACTIVE, CustomerStatusCause.SYSTEM_FINANCE, paymentDate);
    }

    public void makeDailyPayment(Date day){
        // TODO: make batch processing
        CustomerAccountService customerService = getCustomerService();
        TariffAssignmentService tariffService = getTariffService();
        PaymentService paymentService = getPaymentService();
        TextMessages textMessages = getTextMessages();

        Date startDay = DateUtils.startOfDay(day);
        Date midnight = DateUtils.addMinute(startDay, 60);
        Date endDay = DateUtils.endOfDay(day);

        PaymentGroupDataSet paymentGroup = paymentService.openPaymentGroup(textMessages.getDailyPaymentName(day));

        for (CustomerSummeryView customerSummery: customerService.getSummeryList(null, null, null)){

            CustomerDataSet customer = customerSummery.getCustomer();
            TariffDataSet tariff = tariffService.getTariff(customer, endDay);

            if (tariff == null || !tariff.isAutoDailyPayment())
                continue;

            if (!haveActivity(customer, midnight, endDay))
                continue;

            paymentService.addPaymentToGroup(paymentGroup, customer, -tariff.getDailyPayment(), endDay);
        }

        paymentService.closePaymentGroup(paymentGroup);
    }


    private boolean haveActivity(CustomerDataSet customer, Date from, Date until){
        CustomerStatusService statusService = getCustomerStatusService();
        CustomerStatusDataSet status = statusService.getStatus(customer, from);

        if (status != null && status.getStatus() == CustomerStatus.ACTIVE)
            return true;

        DataSetFilter filter = new DataSetFilter();
        filter.add("applyAt", CmpOperator.GT_EQ, from);
        filter.add("applyAt", CmpOperator.LT_EQ, until);
        filter.add("processed", CmpOperator.EQ, true);
        filter.add("status", CmpOperator.EQ, CustomerStatus.ACTIVE);
        filter.add("customerId", CmpOperator.EQ, customer.getId());

        return statusService.getStatusCount(filter) > 0;
    }

    private CustomerStatusService getCustomerStatusService(){
        return application.getByType(CustomerStatusService.class);
    }

    private TariffAssignmentService getTariffService(){
        return application.getByType(TariffAssignmentService.class);
    }

    private PaymentService getPaymentService(){
        return application.getByType(PaymentService.class);
    }

    private CustomerAccountService getCustomerService(){
        return application.getByType(CustomerAccountService.class);
    }

    private TextMessages getTextMessages(){
        return application.getByType(TextMessages.class);
    }
}
