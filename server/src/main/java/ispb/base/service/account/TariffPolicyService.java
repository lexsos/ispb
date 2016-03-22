package ispb.base.service.account;

import ispb.base.db.dataset.PaymentDataSet;

import java.util.Date;


public interface TariffPolicyService {

    void paymentApplied(PaymentDataSet payment);
    void makeDailyPayment(Date day);
}
