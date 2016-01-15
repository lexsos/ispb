package ispb.frontend.rpc.procedure;

import ispb.base.Application;
import ispb.base.db.dao.CityDataSetDao;
import ispb.base.db.dao.StreetDataSetDao;
import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.rpc.RpcArg;
import ispb.base.frontend.rpc.RpcProcedure;
import ispb.base.frontend.utils.AccessLevel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class StreetNameExistRpc extends RpcProcedure {

    private static class StreetNameExistArgs extends RpcArg {

        private String name;
        private long cityId;

        public boolean verify() {
            if (getName() != null && getCityId() > 0)
                return true;
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCityId() {
            return cityId;
        }

        public void setCityId(long cityId) {
            this.cityId = cityId;
        }
    }

    public int getAccessLevel(){
        return AccessLevel.MIN;
    }

    public Class getArgType() {
        return StreetNameExistArgs.class;
    }

    public Object call(HttpServletRequest request,
                       HttpServletResponse response,
                       RpcArg obj,
                       Application application) throws ServletException, IOException {
        StreetNameExistArgs street = (StreetNameExistArgs)obj;
        StreetDataSetDao streetDao = application.getDaoFactory().getStreetDao();
        CityDataSetDao cityDao = application.getDaoFactory().getCityDao();

        CityDataSet city = cityDao.getById(street.getCityId());

        if (city == null)
            return false;

        if (streetDao.getByName(city, street.getName()) == null)
            return false;

        return true;
    }
}
