package amarnehsoft.com.debits.utils;

import amarnehsoft.com.debits.beans.Reminder;

/**
 * Created by jcc on 9/19/2017.
 */

public class ReminderContentBuilder {
    public static String buildContent(Reminder reminder){
        return "reminder "+ reminder.getCode();
    }

    public static String buildTitle(Reminder reminder){
        return "reminder title "+ reminder.getCode();
    }
}
