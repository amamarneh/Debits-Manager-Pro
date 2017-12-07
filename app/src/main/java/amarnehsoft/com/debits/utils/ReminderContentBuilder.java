package amarnehsoft.com.debits.utils;

import android.content.Context;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;

/**
 * Created by jcc on 9/19/2017.
 */

public class ReminderContentBuilder {
    public static String buildContent(Context context, Reminder reminder){
        Person person = PersonsDB.getInstance(context).getBeanById(reminder.getPersonCode());
        String personName = context.getString(R.string.not_found);
        Cur cur = CurDB.getInstance(context).getBeanById(reminder.getCurCode());
        String curName = context.getString(R.string.not_found);
        if (cur != null) curName = cur.getName();

        if (person != null) personName = person.getName();
        return context.getString(R.string.person) + " : " + personName +" \n" +
                context.getString(R.string.amount) + " : " + reminder.getAmount()+" \n" +
                context.getString(R.string.currency) + " : " + curName +" \n" +
                context.getString(R.string.notes) + " : " + reminder.getNotes();

    }

    public static String buildTitle(Context context,Reminder reminder){
        return context.getString(R.string.paymentReminder);
    }
}
