package ispb.base.db.dataset;


import com.google.gson.Gson;
import ispb.base.db.utils.BaseDataSet;
import ispb.base.db.utils.CreatedTimestampable;
import ispb.base.db.utils.DeletedMarkable;
import ispb.base.db.utils.Identifiable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer")
public class CustomerDataSet extends BaseDataSet implements DeletedMarkable, Identifiable, CreatedTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK__customer_building__to__building"))
    private BuildingDataSet building;

    @Column(nullable = false)
    private String room;

    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    @Column(name = "delete_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public CustomerDataSet(){
        this.setId(-1);
    }

    public CustomerDataSet(String name, String surname, BuildingDataSet building, String room, String contractNumber){
        this.setId(-1);
        this.setName(name);
        this.setSurname(surname);
        this.setBuilding(building);
        this.setRoom(room);
        this.setContractNumber(contractNumber);
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

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public BuildingDataSet getBuilding(){
        return this.building;
    }

    public void setBuilding(BuildingDataSet building){
        this.building = building;
    }

    public String getRoom(){
        return this.room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public Date getDeleteAt(){
        return this.deleteAt;
    }

    public void setDeleteAt(Date deleteAt){
        this.deleteAt = deleteAt;
    }

    public Date getCreateAt(){
        return this.createAt;
    }

    public void setCreateAt(Date createAt){
        this.createAt = createAt;
    }

    public String getContractNumber(){
        return this.contractNumber;
    }

    public void setContractNumber(String contractNumber){
        this.contractNumber = contractNumber;
    }

}
