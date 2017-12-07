package amarnehsoft.com.debits.controllers;

import android.content.Context;
import android.support.transition.Transition;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import amarnehsoft.com.debits.beans.Balance;
import amarnehsoft.com.debits.beans.CustomBalance;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;

/**
 * Created by jcc on 8/19/2017.
 */

public class BalancesController {

//    public static List<Balance> getBalances(Context context, int type,String query){
//        Map<String , Map<String, Double>> nestedMap = new HashMap<>();//curCode_(personKey_balance_map)
//        List<Transaction> transactionsList;
//
//        if (query == null || query.length() == 0)
//            transactionsList = (List<Transaction>)TransactionsDB.getInstance(context).getAll(type);
//        else
//            transactionsList = TransactionsDB.getInstance(context).getSearchQueryResult(type,query);
//
//        //get all transactions(type = all)
//        for (Transaction trans : transactionsList){
//            String pc = trans.getPersonCode();
//
//            double amount=0;
//
//            if (trans.getType() == 0)
//                amount = trans.getAmount();
//            else
//                amount = -trans.getAmount();
//
//            String curCode = trans.getCurCode();
//
//            Map<String,Double> innerMap = nestedMap.get(curCode); //personKey_balance_map
//            if (innerMap == null){
//                innerMap = new HashMap<>();
//                innerMap.put(pc,amount);
//                nestedMap.put(curCode,innerMap);
//                Log.e("Amarneh","trans >> null map :: personCode= " + pc + "amount ="+amount + " ,CUR = "+curCode);
//            }else {
//                if (innerMap.get(pc) == null){
//                    innerMap.put(pc,amount);
//                }else{
//                    amount += innerMap.get(pc);
//                    innerMap.put(pc,amount);
//                }
//                Log.e("Amarneh","trans >> not null :: personCode= " + pc + "amount ="+amount + " ,CUR = "+curCode);
//            }
//        }
//
//        List<Balance> result = new ArrayList<>();
//        for (String curCode : nestedMap.keySet()){
//            for (String personCode : nestedMap.get(curCode).keySet()){
//                double amount = nestedMap.get(curCode).get(personCode);
//                Log.e("Amarneh","cur="+curCode+",person="+personCode+"amount="+amount);
//                Balance balance = new Balance();
//                balance.setCode(UUID.randomUUID().toString());
//                balance.setPersonCode(personCode);
//                balance.setAmount(amount);
//                result.add(balance);
//            }
//        }
//
//        Log.e("Amarneh","this is the end");
//
//        return result;
//    }


    /*

    select perason.name , (select sum(amount*) from transactions where personCode  = outer.personCode and type = 0) - (select sum(amount) from transactions where personCode  = outer.personCode and type = 0)
     from persons left join transactions as outer where

        on
     personCode = transaction.PersonCode

     groupBy personeCode,name








     */








    public static List<Balance> getBalances(Context context, int type,String query){
        //this func compute he balance with base cur (nis >> equ=1)
        Map<String, Double> map = new HashMap<>();//(personKey_balance_map)
        List<Transaction> transactionsList;

        if (query == null || query.length() == 0)
            transactionsList = (List<Transaction>)TransactionsDB.getInstance(context).getAll(type);
        else
            transactionsList = TransactionsDB.getInstance(context).getSearchQueryResult(type,query);

//        transactionsList = TransactionsDB.getInstance(context).getSearchQueryResultWithTools(type,query,null,0,0,1,true);
        //get all transactions(type = all)
        for (Transaction trans : transactionsList){
            String pc = trans.getPersonCode();

            double amount=0;

            if (trans.getType() == 0)
                amount = trans.getAmount();
            else
                amount = -trans.getAmount();

            amount = amount*trans.getCurEqu();

            Double amountFromMap = map.get(pc);
            if (amountFromMap == null) amountFromMap = 0.0;
            map.put(pc,amount+amountFromMap);
        }

        List<Balance> result = new ArrayList<>();
            for (String personCode : map.keySet()){
                double amount = map.get(personCode);
                Balance balance = new Balance();
                balance.setCode(UUID.randomUUID().toString());
                balance.setPersonCode(personCode);
                balance.setAmount(amount);
                result.add(balance);
            }
        return result;
    }
    public static double getTotalBalance(Context context,String personCode){
        double debits = TransactionsDB.getInstance(context).getTotalTransaction(personCode,0);
        double payments = TransactionsDB.getInstance(context).getTotalTransaction(personCode,1);
        return debits - payments;
    }
    public static Balance getBalancesForPerson(Context context, int type, String personCode){
        //this func compute he balance with base cur (nis >> equ=1)
        //Map<String, Double> map = new HashMap<>();//(personKey_balance_map)
        List<Transaction> transactionsList;

        if (true)
            transactionsList = (List<Transaction>)TransactionsDB.getInstance(context).getTransactionsOfPerson(personCode,type);

        //get all transactions(type = all)
        double totalAmount=0;
        for (Transaction trans : transactionsList){
            //String pc = trans.getPersonCode();
            double amount = 0;
            if (trans.getType() == 0)
                amount = trans.getAmount();
            else
                amount = -trans.getAmount();

            amount = amount*trans.getCurEqu();

            totalAmount+=amount;
        }

        //List<Balance> result = new ArrayList<>();
        //for (String personCode : map.keySet()){
            //double amount = map.get(personCode);
            Balance balance = new Balance();
            balance.setCode(UUID.randomUUID().toString());
            balance.setPersonCode(personCode);
            balance.setAmount(totalAmount);
        return balance;
        //result.add(balance);
        //}
    }

//    public static List<Balance> getBalancesForPerson(Context context, int type, String personCode){
//        Map<String , Map<String, Double>> nestedMap = new HashMap<>();//curCode_(personKey_balance_map)
//
//        //get all transactions(type = all)
//        for (Transaction trans : (List<Transaction>)TransactionsDB.getInstance(context).getTransactionsOfPerson(personCode,type)){
//            String pc = trans.getPersonCode();
//
//            double amount=0;
//
//            if (trans.getType() == 0)
//                amount = trans.getAmount();
//            else
//                amount = -trans.getAmount();
//
//            String curCode = trans.getCurCode();
//
//
//
//            Map<String,Double> innerMap = nestedMap.get(curCode); //personKey_balance_map
//            if (innerMap == null){
//                innerMap = new HashMap<>();
//                innerMap.put(pc,amount);
//                nestedMap.put(curCode,innerMap);
//                Log.e("Amarneh","trans >> null map :: personCode= " + pc + "amount ="+amount + " ,CUR = "+curCode);
//            }else {
//                if (innerMap.get(pc) == null){
//                    innerMap.put(pc,amount);
//                }else{
//                    amount += innerMap.get(pc);
//                    innerMap.put(pc,amount);
//                }
//                Log.e("Amarneh","trans >> not null :: personCode= " + pc + "amount ="+amount + " ,CUR = "+curCode);
//            }
//        }
//
//        List<Balance> result = new ArrayList<>();
//        for (String curCode : nestedMap.keySet()){
//            for (String personCod : nestedMap.get(curCode).keySet()){
//                double amount = nestedMap.get(curCode).get(personCod);
//                Log.e("Amarneh","cur="+curCode+",person="+personCod+"amount="+amount);
//                Balance balance = new Balance();
//                balance.setCode(UUID.randomUUID().toString());
//                balance.setPersonCode(personCod);
//                balance.setAmount(amount);
//                result.add(balance);
//            }
//        }
//
//        Log.e("Amarneh","this is the end");
//
//        return result;
//    }
}
