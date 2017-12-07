package amarnehsoft.com.debits.db.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.beans.CustomBalance;
import amarnehsoft.com.debits.db.tables.PersonTable;
import amarnehsoft.com.debits.db.tables.TransactionsTable;

/**
 * Created by alaam on 9/25/2017.
 */

public class BalancesDB extends DBHelper<CustomBalance> {
    public BalancesDB(Context context) {
        super(context);
    }

    @Override
    public CustomBalance getBeanById(String id) {
        return null;
    }

    @Override
    protected String getTableName() {
        return null;
    }

    public  List<CustomBalance> getCustomBalances(String query,int orderType,boolean asc){
        //select pp.id,name,(select sum(amount) from balances where type =0 and id = outer.id ) -
        // (select sum(amount) from balances where type = 1 and outer.id = id)  as TOTAL from balances  outer right outer join pp on outer.id = pp.id group by outer.id,pp.id,name
        String t1 = PersonTable.TBL_NAME;
        String t2 = TransactionsTable.TBL_NAME;
        String order;
        if(asc)
            order = " asc";
        else
            order = " desc";

        String sql = "SELECT " + t1 + "." + PersonTable.Cols.KEY + ", " + PersonTable.Cols.NAME +
                " ,COALESCE((SELECT SUM(" + TransactionsTable.Cols.AMOUNT + "*" + TransactionsTable.Cols.CUR_EQU + ") FROM " + t2 + " WHERE " + TransactionsTable.Cols.TYPE + " =  0 " +
                " AND " + TransactionsTable.Cols.PERSON_CODE +" = " + "o." + TransactionsTable.Cols.PERSON_CODE + " ),0) - "+
                " COALESCE((SELECT SUM(" + TransactionsTable.Cols.AMOUNT + "*" + TransactionsTable.Cols.CUR_EQU + ") FROM " + t2 + " WHERE " + TransactionsTable.Cols.TYPE + " =  1 " +
                " AND " + TransactionsTable.Cols.PERSON_CODE +" = " + "o." + TransactionsTable.Cols.PERSON_CODE + " ),0)"+
                " 'totalamount' FROM " + t1 + " left join "+t2 +" as o ON o." + TransactionsTable.Cols.PERSON_CODE + " = " + t1 + "." + PersonTable.Cols.KEY ;

                if(!TextUtils.isEmpty(query))
                    sql =  sql + " WHERE " + PersonTable.Cols.NAME + " LIKE '%" + query + "%' ";
        sql = sql + " GROUP BY o." + TransactionsTable.Cols.PERSON_CODE +", " + t1 + "." + PersonTable.Cols.KEY + ", " + PersonTable.Cols.NAME ;
        if(orderType == 1)
            sql = sql + " ORDER BY totalamount " + order;
        else
            sql = sql + " ORDER BY " + PersonTable.Cols.NAME + order;


//        String sql2 = "select TXT_PERSON_CODE,COALESCE((select sum(DBL_AMOUNT*DBL_CUR_EQU) from transactions_TBL where INT_TYPE = 0 and o.TXT_PERSON_CODE = TXT_PERSON_CODE),0) - COALESCE((select sum(DBL_AMOUNT*DBL_CUR_EQU) from transactions_TBL where INT_TYPE = 1 and o.TXT_PERSON_CODE = TXT_PERSON_CODE),0) from transactions_TBL as o group by TXT_PERSON_CODE";
        List<CustomBalance> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst())
                do {
                    CustomBalance balance = new CustomBalance();
                    balance.setAmount(cursor.getDouble(2));
                    balance.setPersonCode(cursor.getString(0));
                    balance.setName(cursor.getString(1));
                    list.add(balance);
                }while (cursor.moveToNext());


        cursor.close();
        return list;

    }

}
