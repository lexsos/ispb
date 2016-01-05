package ispb.base.frontend.entity;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.utils.RestEntity;

public class CityRestEntity extends RestEntity {

    private String name = null;

    public CityRestEntity(){}

    public CityRestEntity(CityDataSet city){
        setId(city.getId());
        setName(city.getName());
    }

    public boolean verify(){
        if (name != null)
            return true;
        return false;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
