package ispb.base.db.dataset;


import ispb.base.db.container.RadiusUserAttributeContainer;
import ispb.base.db.fieldtype.RadiusAttributeCondition;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "radius_user_attribute")
public class RadiusUserAttributeDataSet implements DeletedMarkable, Identifiable, RadiusUserAttributeContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = -1;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__radius_user_attribute__to__radius_user"))
    private
    RadiusUserDataSet user;

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

    public RadiusUserDataSet getUser() {
        return user;
    }

    public void setUser(RadiusUserDataSet user) {
        this.user = user;
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

    public long getUserId(){
        return user.getId();
    }
}
