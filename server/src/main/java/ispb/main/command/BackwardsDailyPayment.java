package ispb.main.command;


import ispb.base.Application;
import ispb.base.service.account.TariffPolicyService;
import ispb.main.utils.BillingBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackwardsDailyPayment {

    public static void run (String configFile) {
        Date day;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter day in format yyyy-MM-dd:");
            String strDay = bufferRead.readLine();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            day = format.parse(strDay);
        }
        catch (ParseException e){
            System.out.println("Can't parse date");
            return;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }

        Application application = BillingBuilder.build(configFile);
        TariffPolicyService service = application.getByType(TariffPolicyService.class);
        if (service.dailyPaymentExist(day)){
            System.out.println("Auto daily payment for this day already exist");
            return;
        }
        service.makeDailyPaymentBackwards(day);
        System.out.println("Auto daily payment was successfully created");
    }
}
