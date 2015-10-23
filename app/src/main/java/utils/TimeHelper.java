package utils;

import com.google.appengine.repackaged.org.joda.time.LocalDateTime;

/**
 * Created by Ravid on 23/10/2015.
 */
public class TimeHelper {
    public static String getCurrentTime(){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHourOfDay();
        int minute = now.getMinuteOfHour();
        int second = now.getSecondOfMinute();
        int millis = now.getMillisOfSecond();
        String time = hour+":"+minute+":"+second+":"+millis;
        return time;
    }

    public static String getCurrentDate(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();
        String date = day+"/"+month+"/"+year;
        return date;
    }
}
