package ispb.main.command;


import ispb.base.Application;
import ispb.base.service.account.RadiusSessionService;
import ispb.base.utils.DateUtils;
import ispb.main.utils.BillingBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class EraseOldSessions {

    public static void run (String configFile) {
        Date olderThen;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter count of days:");
            int days = Integer.parseInt(bufferRead.readLine().trim());
            olderThen = DateUtils.subDay(new Date(), days);
            System.out.println("Do you really erase RADIUS sessions older then " + olderThen +  " (yes/no): ");
            String s = bufferRead.readLine();
            if (!s.equals("yes"))
                return;
        }
        catch (NumberFormatException e){
            System.out.println("Wrong number format");
            return;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }

        Application application = BillingBuilder.build(configFile);
        RadiusSessionService sessionService = application.getByType(RadiusSessionService.class);
        sessionService.eraseOldSession(olderThen);
        System.out.println("Old RADIUS sessions successfully erased");
    }
}
