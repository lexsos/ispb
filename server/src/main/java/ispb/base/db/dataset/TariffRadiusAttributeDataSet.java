package ispb.base.db.dataset;


import ispb.base.db.container.TariffRadiusAttributeContainer;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tariff_radius_attribute")
public class TariffRadiusAttributeDataSet implements DeletedMarkable, Identifiable, TariffRadiusAttributeContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = -1;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__tariff_radius_attribute__to__tariff"))
    private TariffDataSet tariff;

    @Column(nullable = false)
    private String attributeName;

    @Column(nullable = false)
    private String attributeValue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RadiusAttributeCondition condition;

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

    public TariffDataSet getTariff() {
        return tariff;
    }

    public void setTariff(TariffDataSet tariff) {
        this.tariff = tariff;
    }

    public long getTariffId(){
        return tariff.getId();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public RadiusAttributeCondition getCondition() {
        return condition;
    }

    public void setCondition(RadiusAttributeCondition condition) {
        this.condition = condition;
    }
}
