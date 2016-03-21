package ispb.account;


import ispb.base.Application;
import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.dataset.PaymentDataSet;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.service.account.CustomerStatusService;
import ispb.base.service.account.PaymentService;
import ispb.base.service.account.TariffAssignmentService;
import ispb.base.service.account.TariffPolicyService;

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

    private CustomerStatusService getCustomerStatusService(){
        return application.getByType(CustomerStatusService.class);
    }

    private TariffAssignmentService getTariffService(){
        return application.getByType(TariffAssignmentService.class);
    }

    private PaymentService getPaymentService(){
        return application.getByType(PaymentService.class);
    }
}
