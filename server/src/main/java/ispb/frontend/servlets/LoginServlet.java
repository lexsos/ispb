package ispb.frontend.servlets;

import ispb.base.db.dataset.UserDataSet;
import ispb.base.frontend.request.AuthRestRequest;
import ispb.base.frontend.utils.ResponseCodes;
import ispb.base.service.UserAccountService;
import ispb.frontend.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginServlet extends BaseServlet {

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        this.writeFailMessage(response, "You need use POST method.", ResponseCodes.METHOD_NOT_ALLOWED);
    }

    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException {

        AuthRestRequest authRequest = this.prepareJsonRequest(request, response, AuthRestRequest.class);
        if (authRequest == null)
            return;

        UserAccountService accountService = getApplication().getUserAccountService();
        UserDataSet user = accountService.auth(authRequest.getUserName(), authRequest.getPasswd());
        if (user == null){
            this.writeFailMessage(response, "Wrong login or password.", ResponseCodes.NOT_FOUND);
            return;
        }

        request.getSession().setAttribute("user", user);
        this.writeSuccessMessage(response, "Login successful.");
        return;
    }
}
