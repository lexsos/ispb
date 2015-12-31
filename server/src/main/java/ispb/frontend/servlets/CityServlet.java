package ispb.frontend.servlets;

import ispb.base.db.dataset.CityDataSet;
import ispb.base.frontend.entity.CityRestEntity;
import ispb.base.frontend.response.CityListRestResponse;
import ispb.base.frontend.response.CityRestResponse;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CityServlet extends BaseServlet {

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {
        String[] params = request.getPathInfo().split("/");
        if (params.length > 1){
            int id = 0;
            try {
                id = Integer.parseInt(params[1]);
            }
            catch (Exception e){
                this.writeFailMessage(response, "The id must be integer.", ResponseCodes.DATA_FORMAT_INCOMPATIBLE);
                return;
            }
            CityDataSet city = getApplication().getDaoFactory().getCityDao().getById(id);
            if (city == null){
                this.writeFailMessage(response, "City not found.", ResponseCodes.NOT_FOUND);
                return;
            }
            CityRestResponse resp = new CityRestResponse(city);
            writeJson(response, resp.toJson());
        }
        else {
            CityListRestResponse resp = new CityListRestResponse(getApplication().getDaoFactory().getCityDao().getAll());
            writeJson(response, resp.toJson());
        }
    }
}
