package ispb.base.utils;

import java.util.Calendar;
import java.util.Date;


public class DateUtils {

    public static Date startOfDay(Date date){
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        return start.getTime();
    }

    public static Date endOfDay(Date date){
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.SECOND, 59);
        return start.getTime();
    }

    public static Date addMinute(Date date, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date addSecond(Date date, int second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date subSecond(Date date, int second){
        return addSecond(date, -second);
    }

    public static Date yesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

}
