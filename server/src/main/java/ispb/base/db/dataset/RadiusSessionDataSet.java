package ispb.base.db.dataset;

import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "radius_session")
public class RadiusSessionDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

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
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_session__to__customer"))
    private CustomerDataSet customer;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_session__to__radius_user"))
    private RadiusUserDataSet radiusUser;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_session__to__radius_client"))
    private RadiusClientDataSet radiusClient;

    @Column(name = "start_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "stop_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date stopAt;

    @Column(name = "expire_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireAt;

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

    public CustomerDataSet getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDataSet customer) {
        this.customer = customer;
    }

    public RadiusUserDataSet getRadiusUser() {
        return radiusUser;
    }

    public void setRadiusUser(RadiusUserDataSet radiusUser) {
        this.radiusUser = radiusUser;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getStopAt() {
        return stopAt;
    }

    public void setStopAt(Date stopAt) {
        this.stopAt = stopAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public RadiusClientDataSet getRadiusClient() {
        return radiusClient;
    }

    public void setRadiusClient(RadiusClientDataSet radiusClient) {
        this.radiusClient = radiusClient;
    }
}
