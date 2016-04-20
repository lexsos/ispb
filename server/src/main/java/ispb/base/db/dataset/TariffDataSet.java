package ispb.base.db.dataset;

import ispb.base.db.container.TariffContainer;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "tariff")
public class TariffDataSet extends BaseDataSet implements DeletedMarkable, Identifiable, TariffContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = -1;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(nullable = false)
    private String name;

    @Column(name = "daily_payment", nullable = false)
    private double dailyPayment = 0;

    @Column(name = "auto_daily_payment", nullable = false)
    private boolean autoDailyPayment = false;

    @Column(name = "off_threshold", nullable = false)
    private double offThreshold = 0;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getDeleteAt() {
        return deleteAt;
    }

    @Override
    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    public double getDailyPayment() {
        return dailyPayment;
    }

    public void setDailyPayment(double dailyPayment) {
        this.dailyPayment = dailyPayment;
    }

    public boolean isAutoDailyPayment() {
        return autoDailyPayment;
    }

    public void setAutoDailyPayment(boolean autoDailyPayment) {
        this.autoDailyPayment = autoDailyPayment;
    }

    public double getOffThreshold() {
        return offThreshold;
    }

    public void setOffThreshold(double offThreshold) {
        this.offThreshold = offThreshold;
    }
}
