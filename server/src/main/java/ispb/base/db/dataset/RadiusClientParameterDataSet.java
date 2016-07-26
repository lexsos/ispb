package ispb.base.db.dataset;

import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "radius_client_parameter")
public class RadiusClientParameterDataSet extends BaseDataSet implements DeletedMarkable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_client_parameter__to__radius_client"))
    private RadiusClientDataSet client;

    @Column(nullable = false)
    private String parameter;

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

    public RadiusClientDataSet getClient() {
        return client;
    }

    public void setClient(RadiusClientDataSet client) {
        this.client = client;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
