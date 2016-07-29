package ispb.base.db.dataset;


import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "radius_session_attribute")
public class RadiusSessionAttributeDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "packet_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date packetAt;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_session_attribute__to__radius_session"))
    private RadiusSessionDataSet session;

    @Column(name = "packet_type", nullable = false)
    private long packetType;

    @Column(nullable = false)
    private String attribute;

    @Column(nullable = false)
    private String value;

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

    public long getPacketType() {
        return packetType;
    }

    public void setPacketType(long packetType) {
        this.packetType = packetType;
    }

    public RadiusSessionDataSet getSession() {
        return session;
    }

    public void setSession(RadiusSessionDataSet session) {
        this.session = session;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getPacketAt() {
        return packetAt;
    }

    public void setPacketAt(Date packetAt) {
        this.packetAt = packetAt;
    }
}
