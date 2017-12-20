package amarnehsoft.com.debits.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.db.sqlite.CurDB;

/**
 * Created by alaam on 10/6/2017.
 */

public class SPController {
    private static final String SP_NAME = "db";

    private static final String ARG_EXPORT_KEY = "export_key";
    private static final String ARG_IMPORT_KEY = "import_key";
    private static final String ARG_LOGGED = "logged";
    private static final String ARG_UID = "uid";
    private static final String ARG_SYNC_FREQ = "sync_frequency";
    private static final String ARG_EMAIL_KEY = "email_key";
    private static final String ARG_LANGUAGE="language";
    private static final String ARG_BASE_CUR="baseCur";

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences mSharedPreferencesDefault;
    public static SPController newInstance(Context context){
        return new SPController(context);
    }

    private SPController(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        mSharedPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void setExportDate(String date){
        mSharedPreferencesDefault.edit().putString(ARG_EXPORT_KEY,date).apply();
    }

    public void setEmail(String email){
        mSharedPreferencesDefault.edit().putString(ARG_EMAIL_KEY,email).apply();
    }

    public void setImportDate(String date){
        mSharedPreferencesDefault.edit().putString(ARG_IMPORT_KEY,date).apply();
    }

    public void setLogged(Boolean logged){
        sharedPreferences.edit().putBoolean(ARG_LOGGED,logged).apply();
    }
    public void setUid(String uid){
        sharedPreferences.edit().putString(ARG_UID,uid).apply();
    }

    public String getExportDate(){
        return mSharedPreferencesDefault.getString(ARG_EXPORT_KEY,null);
    }
    public String getImportDate(){
        return mSharedPreferencesDefault.getString(ARG_IMPORT_KEY,null);
    }
    public Boolean getLogged(){
        return sharedPreferences.getBoolean(ARG_LOGGED,false);
    }
    public String getUid(){
        return sharedPreferences.getString(ARG_UID,null);
    }



    public void setLanguage(LanguageController.Language language){
        sharedPreferences.edit().putString(ARG_LANGUAGE,language.toString()).apply();
    }

    public LanguageController.Language getLanguage(){
        String lang = sharedPreferences.getString(ARG_LANGUAGE,null);
        if (lang != null){
            return LanguageController.Language.valueOf(lang);
        }else {
            return LanguageController.getDefaultLanguage();
        }
    }

    public Cur getBaseCur(){
        String curId=sharedPreferences.getString(ARG_BASE_CUR, CurDB.getInstance(mContext).getDefualtBean().getCode());
        if (curId != null) return CurDB.getInstance(mContext).getBeanById(curId);
        return null;
    }

    public void setBaseCur(Cur cur){
        sharedPreferences.edit().putString(ARG_BASE_CUR,cur.getCode()).apply();
    }

    public void clearSharedPreferences(){
        sharedPreferences.edit().clear().apply();
    }

}
