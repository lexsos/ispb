package ispb.base.db.dao;


import ispb.base.db.dataset.AutoPaymentJournalDataSet;

public interface AutoPaymentJournalDataSetDao {
    long save(AutoPaymentJournalDataSet autoPayment);
    void delete(AutoPaymentJournalDataSet autoPayment);
    AutoPaymentJournalDataSet getByPattern(String pattern);
}
