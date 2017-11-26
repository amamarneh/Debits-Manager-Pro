package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

import amarnehsoft.com.debits.db.tables.PersonCatTable;

/**
 * Created by jcc on 8/18/2017.
 */

public class Cur implements Parcelable{
    private String code;
    private String name;
    private double equ;

    public Cur(){}

    protected Cur(Parcel in) {
        code = in.readString();
        name = in.readString();
        equ = in.readDouble();
    }

    public static final Creator<Cur> CREATOR = new Creator<Cur>() {
        @Override
        public Cur createFromParcel(Parcel in) {
            return new Cur(in);
        }

        @Override
        public Cur[] newArray(int size) {
            return new Cur[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEqu() {
        return equ;
    }

    public void setEqu(double equ) {
        this.equ = equ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeDouble(equ);
    }
}
