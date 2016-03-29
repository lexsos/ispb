package ispb.main.command;

import ispb.base.Application;
import ispb.base.service.account.UserAccountService;
import ispb.main.utils.BillingBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResetAdmin {

    public static void run (String configFile) {
        String newPassword;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Do you really reset admin user (yes/no):");
            String s = bufferRead.readLine();
            if (!s.equals("yes"))
                return;
            System.out.println("Enter new password:");
            newPassword = bufferRead.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }

        Application application = BillingBuilder.build(configFile);
        UserAccountService accountService = application.getByType(UserAccountService.class);
        accountService.resetAdmin("admin", newPassword);
        System.out.println("The admin user was successfully reset.");
    }
}
