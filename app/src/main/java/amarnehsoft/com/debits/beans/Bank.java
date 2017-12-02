package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcc on 12/1/2017.
 */

public class Bank implements Parcelable{
    private String code;
    private String name;

    public Bank() {
    }

    protected Bank(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
    }
}
