package amarnehsoft.com.debits.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alaam on 10/6/2017.
 */

public class SharedPreferenceController {
    public static final String DB_NAME = "db";
    public static final String EXPORT_KEY = "export_key";
    public static final String IMPORT_KEY = "import_key";
    public static final String LOGGED = "logged";
    public static final String UID = "uid";
    public static final String SYNC_FREQ = "sync_frequency";
    public static final String EMAIL_KEY = "email_key";

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences defualtSharedPreferenced;
    public SharedPreferenceController(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(DB_NAME,Context.MODE_PRIVATE);
        defualtSharedPreferenced = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    public void setExportDate(String date){
        SharedPreferences.Editor editor = defualtSharedPreferenced.edit();
        editor.putString(EXPORT_KEY,date);
        editor.apply();
    }
    public void setEmail(String email){
        SharedPreferences.Editor editor = defualtSharedPreferenced.edit();
        editor.putString(EMAIL_KEY,email);
        editor.apply();
    }
//    public void setSyncFreq(int frq){
//        SharedPreferences.Editor editor = defualtSharedPreferenced.edit();
//        editor.putInt(SYNC_FREQ,frq);
//        editor.apply();
//    }
    public void setImportDate(String date){
        SharedPreferences.Editor editor = defualtSharedPreferenced.edit();
        editor.putString(IMPORT_KEY,date);
        editor.apply();
    }

    public void setLogged(Boolean logged){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGGED,logged);
        editor.commit();
    }
    public void setUid(String uid){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UID,uid);
        editor.apply();
    }

    public String getExportDate(){
        return defualtSharedPreferenced.getString(EXPORT_KEY,null);
    }
    public String getImportDate(){
        return defualtSharedPreferenced.getString(IMPORT_KEY,null);
    }
    public Boolean getLogged(){
        return sharedPreferences.getBoolean(LOGGED,false);
    }
    public String getUid(){
        return sharedPreferences.getString(UID,null);
    }

    public void clearDefualtPreferences(){
        defualtSharedPreferenced.edit().clear().apply();
    }
    public void clearSharedPreferences(){
        sharedPreferences.edit().clear().apply();
    }

}
