package ispb.base.frontend.rest.response;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.rest.entity.CityRestEntity;
import ispb.base.frontend.rest.utils.RestResponse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CityListRestResponse extends RestResponse {

    private List<CityRestEntity> city_list = null;

    public CityListRestResponse(List<CityDataSet> city_list){
        this.city_list = new LinkedList<CityRestEntity>();
        for (Iterator<CityDataSet> i = city_list.iterator(); i.hasNext(); ){
            CityRestEntity city = new CityRestEntity(i.next());
            this.city_list.add(city);
        }
    }

    public CityListRestResponse(CityDataSet city){
        this.city_list = new LinkedList<CityRestEntity>();
        this.city_list.add(new CityRestEntity(city));
    }

    public List<CityRestEntity> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<CityRestEntity> city_list) {
        this.city_list = city_list;
    }
}
