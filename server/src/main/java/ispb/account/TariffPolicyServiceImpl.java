package ispb.account;


import ispb.base.Application;
import ispb.base.db.dao.AutoPaymentJournalDataSetDao;
import ispb.base.db.dataset.*;
import ispb.base.db.field.CmpOperator;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.filter.DataSetFilter;
import ispb.base.db.utils.DaoFactory;
import ispb.base.db.view.CustomerSummeryView;
import ispb.base.service.LogService;
import ispb.base.service.account.*;
import ispb.base.utils.DateUtils;
import ispb.base.utils.TextMessages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TariffPolicyServiceImpl implements TariffPolicyService {

    private final Application application;
    private final DaoFactory daoFactory;

    public TariffPolicyServiceImpl(Application application, DaoFactory daoFactory){
        this.application = application;
        this.daoFactory = daoFactory;
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

    public void makeDailyPaymentBackwards(Date day){
        makeDailyPaymentImpl(day, true);
    }

    public void makeDailyPayment(Date day){
        makeDailyPaymentImpl(day, false);
    }

    public boolean dailyPaymentExist(Date day){
        return getDailyJournalRecord(day) != null;
    }

    public void deleteDailyPayment(Date day){
        // TODO: write implementation
    }

    private void makeDailyPaymentImpl(Date day, boolean backwards){
        // TODO: make batch processing
        CustomerAccountService customerService = getCustomerService();
        TariffAssignmentService tariffService = getTariffService();
        PaymentService paymentService = getPaymentService();
        TextMessages textMessages = getTextMessages();
        AutoPaymentJournalDataSetDao journalDao = daoFactory.getAutoPaymentJournalDataSetDao();
        LogService log = getLogService();

        Date startDay = DateUtils.startOfDay(day);
        Date midnight = DateUtils.addMinute(startDay, 60);
        Date endDay = DateUtils.endOfDay(day);
        String paymentPattern = getDailyPaymentPattern(startDay);

        if (journalDao.getByPattern(paymentPattern) != null) {
            log.warn("Auto daily payment with pattern " + paymentPattern + " already exist");
            return;
        }

        AutoPaymentJournalDataSet dailyPayment = new AutoPaymentJournalDataSet();
        dailyPayment.setPattern(paymentPattern);
        dailyPayment.setStartAt(new Date());
        journalDao.save(dailyPayment);

        PaymentGroupDataSet paymentGroup = paymentService.openPaymentGroup(textMessages.getDailyPaymentName(day));
        dailyPayment.setPaymentGroup(paymentGroup);
        journalDao.save(dailyPayment);

        for (CustomerSummeryView customerSummery: customerService.getSummeryList(null, null, null)){

            CustomerDataSet customer = customerSummery.getCustomer();
            TariffDataSet tariff = tariffService.getTariff(customer, endDay);

            if (tariff == null || !tariff.isAutoDailyPayment())
                continue;

            if (!haveActivity(customer, midnight, endDay))
                continue;

            paymentService.addPaymentToGroup(paymentGroup, customer, -tariff.getDailyPayment(), endDay, backwards);
        }

        dailyPayment.setFinishAt(new Date());
        journalDao.save(dailyPayment);

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

    private AutoPaymentJournalDataSet getDailyJournalRecord(Date day){
        Date startDay = DateUtils.startOfDay(day);
        String paymentPattern = getDailyPaymentPattern(startDay);
        AutoPaymentJournalDataSetDao journalDao = daoFactory.getAutoPaymentJournalDataSetDao();
        return journalDao.getByPattern(paymentPattern);
    }

    private String getDailyPaymentPattern(Date day){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(day);
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

    private LogService getLogService(){
        return application.getByType(LogService.class);
    }
}
