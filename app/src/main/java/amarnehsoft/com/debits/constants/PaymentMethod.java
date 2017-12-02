package amarnehsoft.com.debits.constants;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.net.ContentHandler;

import amarnehsoft.com.debits.R;

/**
 * Created by jcc on 12/1/2017.
 */

public enum PaymentMethod {
    CASH(0),CHECK(1),CREDIT_CARD(2);
    private int method;
    PaymentMethod(int method){
        this.method = method;
    }
    public int value(){
        return method;
    }
    public static PaymentMethod get(int method){
        switch (method){
            case 0:
                return CASH;
            case 1:
                return CHECK;
            case 2:
                return CREDIT_CARD;
        }
        return CASH;
    }

    public String getString(Context context){
        switch (method){
            case 0:
                return context.getString(R.string.cash);
            case 1:
                return context.getString(R.string.check);
            case 2:
                return context.getString(R.string.creditCard);
        }
        return null;
    }
}
