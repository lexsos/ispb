package ispb.base.service.account;

import ispb.base.db.dataset.PaymentDataSet;


public interface TariffPolicyService {

    void paymentApplied(PaymentDataSet payment);
}
