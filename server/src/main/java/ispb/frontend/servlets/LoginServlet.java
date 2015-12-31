package ispb.frontend.servlets;

import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.rest.RestAuthRequest;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.service.UserAccountService;
import ispb.frontend.utils.RestBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginServlet extends RestBaseServlet {

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        this.writeMessage(response, "You need use POST method.", ResponseCodes.METHOD_NOT_ALLOWED);
    }

    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        RestAuthRequest authRequest = this.prepareJsonRequest(request, response, RestAuthRequest.class);
        if (authRequest == null)
            return;

        UserAccountService accountService = getApplication().getUserAccountService();
        UserDataSet user = accountService.auth(authRequest.getUserName(), authRequest.getPasswd());
        if (user == null){
            this.writeMessage(response, "Wrong login or password.", ResponseCodes.NOT_FOUND);
            return;
        }

        request.getSession().setAttribute("user", user);
        this.writeMessage(response, "Login successful.", ResponseCodes.OK);
        return;
    }
}
