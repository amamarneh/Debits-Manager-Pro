package amarnehsoft.com.debits.beans;

import java.util.Date;

/**
 * Created by alaam on 9/25/2017.
 */

public class CustomBalance {
    private String name;
    private String personCode;
    private double amount;
    private Date date;

    public CustomBalance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
