package ispb.base.db.dataset;

import com.google.gson.Gson;
import javax.persistence.*;
import java.util.Date;

import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;


@Entity
@Table(name = "street")
public class StreetDataSet extends BaseDataSet implements DeletedMarkable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__street_city_to__city"))
    private CityDataSet city;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    public StreetDataSet(){
        this.setId(-1);
    }

    public StreetDataSet(String name, CityDataSet city){
        this.setId(-1);
        this.setName(name);
        this.setCity(city);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CityDataSet getCity(){
        return this.city;
    }

    public void setCity(CityDataSet city){
        this.city = city;
    }

    public Date getDeleteAt(){
        return this.deleteAt;
    }

    public void setDeleteAt(Date deleteAt){
        this.deleteAt = deleteAt;
    }
}
