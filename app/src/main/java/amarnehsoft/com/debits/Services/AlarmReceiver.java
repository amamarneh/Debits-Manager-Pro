package amarnehsoft.com.debits.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import amarnehsoft.com.debits.db.DBTools;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alaam on 10/4/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Amarneh","Alarm Received! - Exporting");
        SharedPreferences sp = context.getSharedPreferences("db",MODE_PRIVATE);
        String uid = sp.getString("uid",null);
        if(uid == null)
            return;
        new DBTools(context,uid,DBTools.MODE_SILENCE).ExportDB();
    }
}
