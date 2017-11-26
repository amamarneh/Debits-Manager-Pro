package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jcc on 8/18/2017.
 */

public class Reminder extends Transaction{
    private Date reminerDate;

    public Date getReminerDate() {
        return reminerDate;
    }

    public void setReminerDate(Date reminerDate) {
        this.reminerDate = reminerDate;
    }

    public Reminder(){super();}

    protected Reminder(Parcel in) {
        super(in);
        reminerDate = new Date(in.readLong());
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeLong(reminerDate.getTime());
    }
}
