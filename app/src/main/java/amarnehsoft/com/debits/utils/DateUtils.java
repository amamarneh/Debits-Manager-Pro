package amarnehsoft.com.debits.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by jcc on 8/22/2017.
 */

public class DateUtils {
    public static Date getDate(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    public static String formatDate(Date date){
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d/MM/yyyy h:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static String formatDateWithoutTime(Date date){
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static Calendar getCalendarFromDate(Date date){
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int compare(Date src,Date dest){
         if (src.before(dest)){
             return -1;
         }

         if (src.after(dest)){
             return 1;
         }

         return 0;
    }

    public static long getDiffDays(Date date1,Date date2) {
        //if date1 > date2 => positive , else => negative

        Calendar calendar1= Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        long msDiff = calendar1.getTimeInMillis()-calendar2.getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        Log.e("Amarneh","diffDays>>"+daysDiff);
        return daysDiff;
    }
}
