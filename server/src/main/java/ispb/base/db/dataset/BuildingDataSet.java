package ispb.base.db.dataset;

import java.util.Date;
import javax.persistence.*;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;


@Entity
@Table(name = "building")
public class BuildingDataSet extends BaseDataSet implements DeletedMarkable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__building_street__to__street"))
    private StreetDataSet street;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    public BuildingDataSet(){
        this.setId(-1);
    }

    public BuildingDataSet(String name, StreetDataSet street){
        this.setId(-1);
        this.setName(name);
        this.setStreet(street);
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public StreetDataSet getStreet(){
        return this.street;
    }

    public void setStreet(StreetDataSet street){
        this.street = street;
    }

    public Date getDeleteAt(){
        return this.deleteAt;
    }

    public void setDeleteAt(Date deleteAt){
        this.deleteAt = deleteAt;
    }

}
