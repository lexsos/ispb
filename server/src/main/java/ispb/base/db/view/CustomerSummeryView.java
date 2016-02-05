package ispb.base.db.view;


import ispb.base.db.dataset.CustomerDataSet;
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

    public CustomerDataSet getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDataSet customer) {
        this.customer = customer;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
