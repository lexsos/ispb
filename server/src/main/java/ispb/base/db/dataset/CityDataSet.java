package ispb.base.db.dataset;

import java.util.Date;
import javax.persistence.*;
import com.google.gson.Gson;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;


@Entity
@Table(name = "city")
public class CityDataSet extends BaseDataSet implements DeletedMarkable, Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String name;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    public CityDataSet() {
        this.setDeleteAt(null);
        this.setId(-1);
    }
    
    public CityDataSet(long id, String name) {
        this.setId(id);
        this.setName(name);
        this.setDeleteAt(null);
    }
    
    public CityDataSet(String name) {
        this.setId(-1);
        this.setDeleteAt(null);
        this.setName(name);
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

    public Date getDeleteAt(){
        return this.deleteAt;
    }

    public void setDeleteAt(Date deleteAt){
        this.deleteAt = deleteAt;
    }

}
