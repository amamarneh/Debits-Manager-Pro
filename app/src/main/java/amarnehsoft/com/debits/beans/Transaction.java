package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jcc on 8/18/2017.
 */

public class Transaction implements Parcelable{
    private String code;
    private int type; //0:me , 1:onMe
    private String personCode;
    private double amount;
    private String curCode;
    private double curEqu;
    private String notes;
    private Date creationDate;
    private Date transDate;

    public Transaction(){}

    protected Transaction(Parcel in) {
        code = in.readString();
        type = in.readInt();
        personCode = in.readString();
        amount = in.readDouble();
        curCode = in.readString();
        notes = in.readString();
        creationDate = new Date(in.readLong());
        transDate = new Date(in.readLong());
        curEqu = in.readDouble();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public double getCurEqu() {
        return curEqu;
    }

    public void setCurEqu(double curEqu) {
        this.curEqu = curEqu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(personCode);
        dest.writeString(curCode);
        dest.writeString(notes);
        dest.writeDouble(amount);
        dest.writeInt(type);
        dest.writeLong(creationDate.getTime());
        dest.writeLong(transDate.getTime());
        dest.writeDouble(curEqu);
    }
}
