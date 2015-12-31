package ispb.base.frontend.response;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.entity.CityRestEntity;
import ispb.base.frontend.utils.RestResponse;

public class CityRestResponse extends RestResponse {
    private CityRestEntity city;

    public CityRestResponse(CityDataSet city){
        this.city = new CityRestEntity(city);
    }

    public CityRestEntity getCity() {
        return city;
    }
    public void setCity(CityRestEntity city) {
        this.city = city;
    }
}
