package ispb.frontend.servlets;

import ispb.base.frontend.rest.RestAuthRequest;
import ispb.frontend.utils.RestBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthServlet extends RestBaseServlet {

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        this.writeMessage(response, "You need use POST method.", 405);
    }

    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        RestAuthRequest auth = this.prepareJsonRequest(request, response, RestAuthRequest.class);
        if (auth == null)
            return;
    }
}
