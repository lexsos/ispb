package ispb.base.db.view;


import ispb.base.db.dataset.CustomerDataSet;
import ispb.base.db.dataset.TariffDataSet;
import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;

@Entity
@Table(name = "customer_view")
public class CustomerSummeryView extends BaseDataSet implements Identifiable {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CustomerDataSet customer;

    @Column
    private double balance;

    @ManyToOne
    @JoinColumn(nullable = true)
    private TariffDataSet tariff;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    public CustomerDataSet getCustomer() {
        return customer;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public TariffDataSet getTariff() {
        return tariff;
    }

    public CustomerStatus getStatus() {
        return status;
    }
}
