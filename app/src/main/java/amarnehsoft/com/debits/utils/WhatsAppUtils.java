package amarnehsoft.com.debits.utils;

import android.content.Context;
import android.content.Intent;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;

/**
 * Created by jcc on 12/12/2017.
 */

public class WhatsAppUtils {
    public static Intent send(String phone, String content) {
        String smsNumber = phone; // E164 format without '+' sign
        //Uri uri = Uri.parse("smsto:" + smsNumber); 972598475054
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        sendIntent = Intent.createChooser(sendIntent, "");
        return sendIntent;
    }

    public static String generateContent(Context context,Person person){
        String content="";
        content+=context.getString(R.string.dear) + " " + person.getName() + "," + "\n";
        content+=context.getString(R.string.thisReportWasGeneratedByDebitsManagerApp) +"\n";
        Cur cur = CurDB.getInstance(context).getDefualtBean();
        double debits = TransactionsDB.getInstance(context).getTotalTransaction(person.getKey(),0);
        double payments = TransactionsDB.getInstance(context).getTotalTransaction(person.getKey(),1);
        double balance = BalancesController.getTotalBalance(context,person.getKey());
        String balanceStr = "";
        if (balance > 0){
            balanceStr = context.getString(R.string.debits);
        }
        if (balance < 0){
            balanceStr = context.getString(R.string.payments);
        }
        content+=context.getString(R.string.theDebits) + " : " + debits + " " + cur.getName() + "\n";
        content+=context.getString(R.string.thePayments) + " : " + payments + " " + cur.getName() + "\n";
        content+=context.getString(R.string.theBalance) + " : " + balance + " " + cur.getName() + ", "+ balanceStr + "\n\n";
        content+=context.getString(R.string.thanks);
        return content;
    }
}
