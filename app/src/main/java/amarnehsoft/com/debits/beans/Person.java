package amarnehsoft.com.debits.beans;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcc on 5/31/2017.
 */

public class Person implements Parcelable {
    private String key;
    private String name;
    private String phone;
    private String email;
    private String address;
    private int isDeleted=0;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    //    private double balance;
    private String catCode;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    protected Person(Parcel in) {
        key = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

//    public double getBalance() {
//        return balance;
//    }

//    public void setBalance(double balance) {
//        this.balance = balance;
//    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(catCode);
    }
}