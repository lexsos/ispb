package ispb.base.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextMessages {

    private String initPaymentName;
    private String dailyPaymentName;

    public String getInitPaymentName(String contractNumber) {
        String result = initPaymentName;
        result = result.replaceAll("\\{contract_number\\}", contractNumber);
        return result;
    }

    public String getDailyPaymentName(Date day){
        String result = dailyPaymentName;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        result = result.replaceAll("\\{day\\}", df.format(day));
        return result;
    }
}
