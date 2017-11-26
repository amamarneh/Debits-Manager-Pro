package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcc on 8/19/2017.
 */

public class Balance implements Parcelable{
    private String code;
    private String personCode;
    private double amount;
    private int type;
    private String notes;

    public Balance(Parcel in) {
        code = in.readString();
        personCode = in.readString();
        amount = in.readDouble();
        type = in.readInt();
        notes = in.readString();
    }

    public static final Creator<Balance> CREATOR = new Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel in) {
            return new Balance(in);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
        }
    };

    public Balance() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(personCode);
        dest.writeString(notes);
        dest.writeDouble(amount);
        dest.writeInt(type);
    }
}
