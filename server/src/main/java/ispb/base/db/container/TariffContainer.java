package ispb.base.db.container;


import java.util.Date;

public interface TariffContainer {
    long getId();
    Date getDeleteAt();
    String getName();
    double getDailyPayment();
    boolean isAutoDailyPayment();
    double getUpRate();
    double getDownRate();
    double getOffThreshold();
}
