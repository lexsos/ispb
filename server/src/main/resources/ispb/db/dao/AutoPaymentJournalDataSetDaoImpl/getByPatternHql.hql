from AutoPaymentJournalDataSet as autoPaymentJournal
where
    autoPaymentJournal.pattern = :pattern and
    autoPaymentJournal.deleteAt is null