package amarnehsoft.com.debits.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;

/**
 * Created by jcc on 10/26/2017.
 */

public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];


            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);

            double debits = TransactionsDB.getInstance(context).getSumOfTransaction(0);
            double payments = TransactionsDB.getInstance(context).getSumOfTransaction(1);
            double balance = debits - payments;
            remoteViews.setTextViewText(R.id.txtDebits,debits + "");
            remoteViews.setTextViewText(R.id.txtPayments,payments + "");
            remoteViews.setTextViewText(R.id.txtBalance,balance + "");

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.mainView, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}