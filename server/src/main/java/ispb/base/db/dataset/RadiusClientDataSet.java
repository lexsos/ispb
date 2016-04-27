package ispb.base.db.dataset;


import ispb.base.db.fieldtype.RadiusClientType;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "radius_client")
public class RadiusClientDataSet extends BaseDataSet implements DeletedMarkable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(nullable = false)
    private String ip4Address;

    @Column(nullable = false)
    private String secret;

    @Column(nullable = false)
    private boolean addAuthRequest = false;

    @Column(nullable = false)
    private boolean rejectInactive = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RadiusClientType clientType = RadiusClientType.DEFAULT;

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

    public String getIp4Address() {
        return ip4Address;
    }

    public void setIp4Address(String ip4Address) {
        this.ip4Address = ip4Address;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isAddAuthRequest() {
        return addAuthRequest;
    }

    public void setAddAuthRequest(boolean addAuthRequest) {
        this.addAuthRequest = addAuthRequest;
    }

    public boolean isRejectInactive() {
        return rejectInactive;
    }

    public void setRejectInactive(boolean rejectInactive) {
        this.rejectInactive = rejectInactive;
    }

    public RadiusClientType getClientType() {
        return clientType;
    }

    public void setClientType(RadiusClientType clientType) {
        this.clientType = clientType;
    }
}
