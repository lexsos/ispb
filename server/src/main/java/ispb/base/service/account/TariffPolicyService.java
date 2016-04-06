package ispb.base.service.account;

import ispb.base.db.dataset.CustomerStatusDataSet;
import ispb.base.db.dataset.PaymentDataSet;

import java.util.Date;


public interface TariffPolicyService {

    void paymentApplied(PaymentDataSet payment);
    void beforeStatusApplied(CustomerStatusDataSet status);

    void makeDailyPayment(Date day);
    void makeDailyPaymentBackwards(Date day);
    boolean dailyPaymentExist(Date day);
    void deleteDailyPayment(Date day);
}
