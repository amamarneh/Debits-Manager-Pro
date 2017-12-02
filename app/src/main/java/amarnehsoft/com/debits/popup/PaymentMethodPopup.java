package amarnehsoft.com.debits.popup;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.constants.PaymentMethod;

/**
 * Created by jcc on 12/2/2017.
 */

public class PaymentMethodPopup extends PopupMenu {
    private IPaymentMethodPopup mListener;
    public PaymentMethodPopup(Context context, View anchor,IPaymentMethodPopup listener) {
        super(context, anchor);
        mListener = listener;
        getMenuInflater().inflate(R.menu.poupup_menu, getMenu());

        //registering popup with OnMenuItemClickListener
        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null){
                    if (item.getItemId() == R.id.itemCash) mListener.setPaymentMethod(PaymentMethod.CASH);
                    if (item.getItemId() == R.id.itemCheck) mListener.setPaymentMethod(PaymentMethod.CHECK);
                    if (item.getItemId() == R.id.itemCreditCard) mListener.setPaymentMethod(PaymentMethod.CREDIT_CARD);
                }
                return true;
            }
        });
    }

    public interface IPaymentMethodPopup{
        void setPaymentMethod(PaymentMethod paymentMethod);
    }
}
