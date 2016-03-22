package ispb.base.db.dataset;


import ispb.base.db.fieldtype.CustomerStatus;
import ispb.base.db.fieldtype.CustomerStatusCause;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer_status")
public class CustomerStatusDataSet extends BaseDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "apply_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyAt;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatusCause cause;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__customer_status__to__customer"))
    private CustomerDataSet customer;

    public CustomerStatusDataSet(){
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public CustomerStatusCause getCause() {
        return cause;
    }

    public void setCause(CustomerStatusCause cause) {
        this.cause = cause;
    }

    public CustomerDataSet getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDataSet customer) {
        this.customer = customer;
    }
}
