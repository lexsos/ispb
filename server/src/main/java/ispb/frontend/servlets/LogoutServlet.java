package ispb.frontend.servlets;

import ispb.base.frontend.utils.ResponseCodes;
import ispb.frontend.utils.RestBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends RestBaseServlet {
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        request.getSession().setAttribute("user", null);
        this.writeMessage(response, "Logout successful.", ResponseCodes.OK);
    }
}
