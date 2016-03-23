package ispb.base.db.dataset;


import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "auto_payment_journal")
public class AutoPaymentJournalDataSet  extends BaseDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = -1;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @ManyToOne
    @JoinColumn(nullable = true, foreignKey = @ForeignKey(name = "FK__auto_payment_journal__to__payment_group"))
    private PaymentGroupDataSet paymentGroup;

    @Column(nullable = false)
    private String pattern;

    @Column(name = "start_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "finish_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishAt;

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

    public PaymentGroupDataSet getPaymentGroup() {
        return paymentGroup;
    }

    public void setPaymentGroup(PaymentGroupDataSet paymentGroup) {
        this.paymentGroup = paymentGroup;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }
}
