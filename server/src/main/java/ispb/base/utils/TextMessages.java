package ispb.base.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextMessages {

    private String initPaymentName;
    private String dailyPaymentName;
    private String dailyPaymentDateFormat;

    public String getInitPaymentName(String contractNumber) {
        String result = initPaymentName;
        result = result.replaceAll("\\{contract_number\\}", contractNumber);
        return result;
    }

    public String getDailyPaymentName(Date day){
        String result = dailyPaymentName;
        DateFormat df = new SimpleDateFormat(dailyPaymentDateFormat);
        result = result.replaceAll("\\{day\\}", df.format(day));
        return result;
    }
}
