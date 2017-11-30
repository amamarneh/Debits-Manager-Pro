package amarnehsoft.com.debits;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import amarnehsoft.com.debits.beans.Balance;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonCatsDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.RemindersDB;
import amarnehsoft.com.debits.db.TransactionsDB;

/**
 * Created by jcc on 8/18/2017.
 */
//a
public class Dummy {
    public static void setDummyData(Context ctx){
        PersonsDB.getInstance(ctx).deleteAll();
        CurDB.getInstance(ctx).deleteAll();
        TransactionsDB.getInstance(ctx).deleteAll();

        List<Person> personList = new ArrayList<>();
        List<PersonCat> personCatList = new ArrayList<>();

        for(int i=0;i<=5;i++){
            PersonCat personCat = new PersonCat();
            String key= UUID.randomUUID().toString();
            personCat.setCode(key);
            personCat.setName("PersonCat#"+i);
            personCatList.add(personCat);

            String curCode=null;

            for(int j=0;j<=10;j++){
                Person person = new Person();
                String personKey= UUID.randomUUID().toString();
                person.setKey(personKey);
                person.setName("Person#"+j+",c"+i);
                person.setEmail("email"+i);
                person.setPhone("phone"+i);
                person.setCatCode(key);
                personList.add(person);


                if (j==0){
                    List<Cur> curList = new ArrayList<>();
                    Cur cur = new Cur();
                    curCode = UUID.randomUUID().toString();
                    cur.setCode(curCode);
                    cur.setName("cur#"+i);
                    cur.setEqu(i+0.5);
                    curList.add(cur);
                    CurDB.getInstance(ctx).saveList(curList,false);
                }
                List<Transaction> transactionList = new ArrayList<>();
                for (int k = 0; k < 10 ; k++){
                    Transaction transaction = new Transaction();
                    transaction.setCode(UUID.randomUUID().toString());
                    transaction.setPersonCode(personKey);
                    transaction.setTransDate(new Date());
                    transaction.setCreationDate(new Date());
                    transaction.setType(k%2);
                    transaction.setAmount(k*12.5);
                    transaction.setCurCode(curCode);
                    transaction.setNotes("notes#"+k);
                    transactionList.add(transaction);
                }
                TransactionsDB.getInstance(ctx).saveList(transactionList,false);

            }
            PersonsDB.getInstance(ctx).saveList(personList,false);
        }
        PersonCatsDB.getInstance(ctx).saveList(personCatList,true);

        BalancesController.getBalances(ctx,-1,null);



        List<Reminder> remindersList = new ArrayList<>();
        for (int k = 0; k < 10 ; k++){
            Reminder transaction = new Reminder();
            transaction.setCode(UUID.randomUUID().toString());
            transaction.setPersonCode("personCode#"+k);
            transaction.setTransDate(new Date());
            transaction.setCreationDate(new Date());
            transaction.setReminerDate(new Date());
            transaction.setType(k%2);
            transaction.setAmount(k*12.5);
            transaction.setCurCode("curCode#"+k);
            transaction.setNotes("notes#"+k);
            remindersList.add(transaction);
        }
        RemindersDB.getInstance(ctx).saveList(remindersList,true);

        for (Reminder r : (List<Reminder>)RemindersDB.getInstance(ctx).getAll(-1)){
            Log.e("Amarneh","reminder#"+r.getCode());
        }
    }

    public static List<Balance> dummyBalances(int type){
        List<Balance> balances = new ArrayList<>();
        type+=2;
        for (int i = 0; i < 50 ; i+=type){
            Balance balance = new Balance();
            balance.setCode(UUID.randomUUID().toString());
            balance.setNotes("notes#"+i);
            balance.setAmount(i*2.3);
            balance.setType(type);
            balance.setPersonCode("dummyPersonCode");
            balances.add(balance);
        }
        return balances;
    }//
}
