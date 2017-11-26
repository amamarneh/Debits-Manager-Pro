package amarnehsoft.com.debits.utils;

/**
 * Created by jcc on 6/13/2017.
 */

public class NumberUtils {

    public static double getDouble(String value){
        double d=0;
        try {
            d = Double.parseDouble(value);
        }catch (Exception ex){
            d=0;
        }
        return d;
    }

    public static int getInteger(String val){
        int i = 0;
        try {
            i = Integer.parseInt(val);
        }catch (Exception e){
            i = 0;
        }
        return i;
    }

    public static double Round(double d){
        double roundOff = Math.round(d*100.0)/100.0;
        return roundOff;
//        return d;
    }
}
