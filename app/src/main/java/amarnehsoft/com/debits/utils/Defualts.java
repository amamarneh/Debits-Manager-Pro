package amarnehsoft.com.debits.utils;

import android.content.Context;

import amarnehsoft.com.debits.R;

/**
 * Created by jcc on 8/21/2017.
 */

public class Defualts {
    public static final String DEFUALT="default";
    public static String catName(Context context){
        return context.getString(R.string.defualt_cat_name);
    }

    public static String curName(Context context){
        return context.getString(R.string.defualt_cur_name);
    }
}
