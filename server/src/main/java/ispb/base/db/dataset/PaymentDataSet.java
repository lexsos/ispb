package ispb.base.db.dataset;

import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "payment")
public class PaymentDataSet extends BaseDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__payment__to__payment_group"))
    private PaymentGroupDataSet group;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__payment__to__customer"))
    private CustomerDataSet customer;

    @Column(nullable = false)
    private double paymentSum = 0;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "apply_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyAt;

    public PaymentDataSet(){
        setId(-1);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Date getDeleteAt() {
        return deleteAt;
    }

    @Override
    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public Date getCreateAt() {
        return createAt;
    }

    @Override
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public double getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(double paymentSum) {
        this.paymentSum = paymentSum;
    }

    public PaymentGroupDataSet getGroup() {
        return group;
    }

    public void setGroup(PaymentGroupDataSet group) {
        this.group = group;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public CustomerDataSet getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDataSet customer) {
        this.customer = customer;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }
}
