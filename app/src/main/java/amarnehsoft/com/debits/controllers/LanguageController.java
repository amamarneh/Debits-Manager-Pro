package amarnehsoft.com.debits.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amarneh on 1/20/2017.
 */

public class LanguageController {

    public enum Language{
        ARABIC("ARABIC"), ENGLISH("ENGLISH");
        private final String text;

        private Language(String text){this.text = text;}

        @Override
        public String toString() {
            return text;
        }
    }

    public static void arabic(Context ctx){
        changeLang(Language.ARABIC,ctx);
    }
    public static void english(Context ctx){
        changeLang(Language.ENGLISH,ctx);
    }

    public static Language getDefaultLanguage()
    {
        Locale locale = Locale.getDefault();
        if(locale == Locale.ENGLISH){
            return Language.ENGLISH;
        }
        return Language.ARABIC;
    }

    public static void refreshLang(Context context) {
        changeLang(SPController.newInstance(context).getLanguage(),context);
    }

    private static Locale getLocale(Language lang){
        if (lang == Language.ARABIC){
            return new Locale("ar");
        }else{
            return Locale.ENGLISH;
        }
    }

    public static void changeLang(Language languageToLoad, Context context) {
        Locale locale = getLocale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, context.getApplicationContext().getResources().getDisplayMetrics());
        SPController.newInstance(context).setLanguage(languageToLoad);
    }
}
