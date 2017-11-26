package amarnehsoft.com.debits.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonCat implements Parcelable{
    private String code;
    private String name;

    public PersonCat(){}

    protected PersonCat(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<PersonCat> CREATOR = new Creator<PersonCat>() {
        @Override
        public PersonCat createFromParcel(Parcel in) {
            return new PersonCat(in);
        }

        @Override
        public PersonCat[] newArray(int size) {
            return new PersonCat[size];
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
