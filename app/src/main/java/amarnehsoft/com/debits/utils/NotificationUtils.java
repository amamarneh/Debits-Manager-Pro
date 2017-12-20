package amarnehsoft.com.debits.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.MainActivity;
import amarnehsoft.com.debits.broadcastReceiver.NotificationPublisher;

/**
 * Created by Amarneh on 21/06/2017.
 */

public class NotificationUtils {
    public static void scheduleNotification(Context context, Notification notification, long delay,int id) {
        Intent intent = NotificationPublisher.newIntent(context,notification,id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent , PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static Notification getNotification(Context context,String title,String content) {
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.ic_add_white_24dp);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.text, content);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_add_white_24dp)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content));
//                .setContent(contentView);



        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        Notification notification = mBuilder.build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        return notification;
    }
}
