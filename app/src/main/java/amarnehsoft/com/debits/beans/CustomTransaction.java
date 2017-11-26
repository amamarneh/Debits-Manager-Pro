package amarnehsoft.com.debits.beans;

import android.os.Parcel;

/**
 * Created by alaam on 9/28/2017.
 */

public class CustomTransaction extends Transaction {
    private String PersonName;
    private String CurName;
    public static final Creator<CustomTransaction> CREATOR = new Creator<CustomTransaction>() {
        @Override
        public CustomTransaction createFromParcel(Parcel in) {
            return new CustomTransaction(in);
        }

        @Override
        public CustomTransaction[] newArray(int size) {
            return new CustomTransaction[size];
        }
    };
    public CustomTransaction(){}
    protected CustomTransaction(Parcel in){
        super(in);
        PersonName = in.readString();
        CurName = in.readString();
    }
    public String getPersonName() {
        return PersonName;
    }

    public String getCurName() {
        return CurName;
    }

    public void setCurName(String curName) {
        CurName = curName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(PersonName);
        dest.writeString(CurName);
    }
}
