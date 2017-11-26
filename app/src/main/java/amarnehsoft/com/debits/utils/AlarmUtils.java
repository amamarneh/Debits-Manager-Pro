package amarnehsoft.com.debits.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import amarnehsoft.com.debits.Services.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.LAUNCHER_APPS_SERVICE;

/**
 * Created by alaam on 10/4/2017.
 */

public class AlarmUtils {
    public final static int CODE_EXPORT = 1;
    public static void AddAlarm(Context context,int code,long freq){
        Log.d("Amarneh","Adding Alarm - freq = " + freq);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, 0);
        long delay = 1000*5;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freq,freq,pendingIntent);
    }
    public static void DeleteAlarm(Context context,int code){
        Log.d("Amarneh","Deleting Alarm");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
