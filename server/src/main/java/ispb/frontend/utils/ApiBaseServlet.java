package ispb.frontend.utils;

import ispb.base.frontend.utils.Verifiable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ApiBaseServlet<T extends Verifiable> extends BaseServlet {

    protected int minAccessLevel(){
        return 0;
    }

    protected abstract Class<T> getRestType();

    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException{

    }

    @Override
    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response ) throws ServletException, IOException {

        T reqObj = this.prepareJsonRequest(request, response, getRestType());
        if (reqObj == null)
            return;
    }

}
