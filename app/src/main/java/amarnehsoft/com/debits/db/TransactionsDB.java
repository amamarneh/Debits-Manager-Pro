package amarnehsoft.com.debits.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.CustomBalance;
import amarnehsoft.com.debits.beans.CustomTransaction;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.tables.CurTable;
import amarnehsoft.com.debits.db.tables.PersonTable;
import amarnehsoft.com.debits.db.tables.TransactionsTable;

/**
 * Created by Amarneh on 6/3/2017.
 */

public class TransactionsDB<B extends Transaction,T extends TransactionsTable> extends DBHelper<Transaction>{

    private static TransactionsDB instance;
    public static TransactionsDB getInstance(Context context){
        if(instance == null){
            instance = new TransactionsDB<>(context);
        }
        return instance;
    }

    private TransactionsDB(Context context) {
        super(context,Transaction.class);
    }
    public int getNoOfTransactionsForPerson(String key){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + T.TBL_NAME + " WHERE " + T.Cols.PERSON_CODE + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{key});
            if(cursor.moveToFirst())
                count = cursor.getInt(0);
        }finally {
            db.close();
        }
        return count;
    }
    public int getNoOfTransactionsForCur(String key){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + T.TBL_NAME + " WHERE " + T.Cols.CUR_CODE + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{key});
            if(cursor.moveToFirst())
                count = cursor.getInt(0);

        }finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return count;
    }
    public List<B> getAll(int type)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String selection=null;
        String[] args = null;

        if (type != -1){
            selection = T.Cols.TYPE+" =?";
            args = new String[]{type+""};
        }

        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , selection, args , null, null, null);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    B bean = (B)newBean();
                    fillBeanFromCursor(rs, bean);
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    public void DeleteAllForPerson(String personCode){
        String sql = "DELETE FROM " + T.TBL_NAME + " WHERE " + T.Cols.PERSON_CODE + " = '" + personCode + "'";
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.execSQL(sql);
            Log.d("Amarneh","Deleteing!");
        }finally {
            db.close();
        }

    }

    public List<B> getSearchQueryResult(int type,String query)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String t = "t";
        String typeQuery = "";
        if (type != -1){
            typeQuery = " AND " + t +"." + T.Cols.TYPE + " =" + type;
        }

//        String sql = "SELECT "+
//                "t."+ T.Cols.CODE + ", "+
//                "t."+ T.Cols.PERSON_CODE + ", "+
//                "t."+ T.Cols.TYPE + ", "+
//                "t."+ T.Cols.AMOUNT + ", "+
//                "t."+ T.Cols.CREATION_DATE + ", "+
//                "t."+ T.Cols.CUR_CODE + ", "+
//                "t."+ T.Cols.CUR_EQU + ", "+
//                "t."+ T.Cols.NOTES + ", "+
//                "t."+ T.Cols.TRANS_DATE +
//                " FROM " + T.TBL_NAME  + " as t , " + PersonTable.TBL_NAME  +
//                " WHERE " + t+ "." + T.Cols.PERSON_CODE + " = " + PersonTable.TBL_NAME+ "." + PersonTable.Cols.KEY + " AND " + PersonTable.TBL_NAME+"." + PersonTable.Cols.NAME  + " LIKE '%"+ query + "%'" + typeQuery +
//                " GROUP BY " + t+"." + T.Cols.CODE + " ORDER BY " + PersonTable.TBL_NAME+"."+PersonTable.Cols.NAME;

        String sql = "SELECT "+
                "t."+ T.Cols.CODE + ", "+
                "t."+ T.Cols.PERSON_CODE + ", "+
                "t."+ T.Cols.TYPE + ", "+
                "t."+ T.Cols.AMOUNT + ", "+
                "t."+ T.Cols.CREATION_DATE + ", "+
                "t."+ T.Cols.CUR_CODE + ", "+
                "t."+ T.Cols.CUR_EQU + ", "+
                "t."+ T.Cols.NOTES + ", "+
                "t."+ T.Cols.TRANS_DATE +
                " FROM " + T.TBL_NAME  + " as t WHERE " + t+ "." + T.Cols.PERSON_CODE + " IN (" + "SELECT " +  PersonTable.TBL_NAME+ "." + PersonTable.Cols.KEY +
                " FROM " + PersonTable.TBL_NAME
                + " WHERE " + PersonTable.TBL_NAME+"." + PersonTable.Cols.NAME  + " LIKE '%"+ query + "%' ORDER BY "+ PersonTable.TBL_NAME+"."+PersonTable.Cols.NAME +")" + typeQuery;
        Cursor rs = null;
        try {
            rs = db.rawQuery(sql, null);

            Log.e("Amarneh","sql="+sql);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    B bean = (B)newBean();
                    fillBeanFromCursor(rs, bean);
                    //bean.setCode(rs.getString(rs.getColumnIndex(T.Cols.CODE)));
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    public List<B> getSearchQueryResultWithTools(int type,String s,String personCode,long fromdate,long todate,int orderby,boolean asc)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String typeQuery = "";
        if (type != -1){
            typeQuery = " AND " + T.Cols.TYPE + " =" + type;
        }
        String query = "SELECT * FROM " + T.TBL_NAME + " WHERE 1=1";
        if(!TextUtils.isEmpty(personCode))
            query = query + " AND " + T.Cols.PERSON_CODE + " = '" + personCode + "' ";
        if(fromdate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " >= '" + fromdate + "' ";
        if(todate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " <= '" + todate + "' ";

        if(!TextUtils.isEmpty(s)) { //there is a searching
            String sql = " AND " + T.Cols.PERSON_CODE + " IN (" + "SELECT " + PersonTable.TBL_NAME + "." + PersonTable.Cols.KEY +
                    " FROM " + PersonTable.TBL_NAME
                    + " WHERE " + PersonTable.TBL_NAME + "." + PersonTable.Cols.NAME + " LIKE '%" + s + "%')";


            query = query + sql;
        }
        query = query + typeQuery;

        if(orderby == 1) {
            query = query + " ORDER BY " + T.Cols.TRANS_DATE;
            if(asc)
                query = query + " ASC";
            else
                query = query + " DESC";
        }
        if(orderby == 2) {
            query = query + " ORDER BY " + T.Cols.AMOUNT;
            if(asc)
                query = query + " ASC";
            else
                query = query + " DESC";
        }

        Cursor rs = null;
        try {
            rs = db.rawQuery(query,null);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    B bean = (B)newBean();
                    fillBeanFromCursor(rs, bean);
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    public List<CustomTransaction> getCustomTransactionsWithTools(int type, String s, String personCode, long fromdate, long todate, int orderby, boolean asc)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<CustomTransaction> list = new ArrayList<>();
        String typeQuery = "";
        if (type != -1){
            typeQuery = " AND " + T.Cols.TYPE + " =" + type;
        }
        String query = "SELECT "
                + PersonTable.TBL_NAME + "." + PersonTable.Cols.NAME + ", "
                + T.Cols.TRANS_DATE  + ", "
                + T.Cols.AMOUNT + ", "
                + T.TBL_NAME + "." +T.Cols.CODE + ", "
                + CurTable.TBL_NAME + "." +CurTable.Cols.NAME + ", "
                + T.Cols.TYPE + ", "
                + T.Cols.PAYMENT_METHOD
                + " FROM " + T.TBL_NAME + " join "+ PersonTable.TBL_NAME+ " on " + T.Cols.PERSON_CODE + " = " + PersonTable.TBL_NAME + "." +PersonTable.Cols.KEY
                + " left join " + CurTable.TBL_NAME + " on " + T.Cols.CUR_CODE + " = " + CurTable.TBL_NAME + "." + CurTable.Cols.CODE
                + "  WHERE 1=1 ";
        if(!TextUtils.isEmpty(personCode))
            query = query + " AND " + T.Cols.PERSON_CODE + " = '" + personCode + "' ";
        if(fromdate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " >= '" + fromdate + "' ";
        if(todate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " <= '" + todate + "' ";

        if(!TextUtils.isEmpty(s)) { //there is a searching
            String sql = " AND " + T.Cols.PERSON_CODE + " IN (" + "SELECT " + PersonTable.TBL_NAME + "." + PersonTable.Cols.KEY +
                    " FROM " + PersonTable.TBL_NAME
                    + " WHERE " + PersonTable.TBL_NAME + "." + PersonTable.Cols.NAME + " LIKE '%" + s + "%')";


            query = query + sql;
        }
        query = query + typeQuery;

        String order;
        if(asc)
            order= " ASC";
        else
            order=" DESC";

        if(orderby == 1) {
            query = query + " ORDER BY " + T.Cols.TRANS_DATE;
            query = query + order;
        }
        if(orderby == 2) {
            query = query + " ORDER BY " + T.Cols.AMOUNT;
            query = query + order;
        }
        if(orderby == 3) {
            query = query + " ORDER BY " + PersonTable.TBL_NAME + "." + PersonTable.Cols.NAME;
            query = query + order;
        }


        Cursor rs = null;
        try {
            rs = db.rawQuery(query,null);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    CustomTransaction transaction = new CustomTransaction();
                    transaction.setCode(rs.getString(3));
                    transaction.setPersonName(rs.getString(0));
                    transaction.setTransDate(new Date(rs.getLong(1)));
                    transaction.setAmount(rs.getDouble(2));
                    transaction.setType(rs.getInt(5));
                    transaction.setCurName(rs.getString(4));
                    transaction.setPaymentMethod(rs.getInt(6));
                    list.add(transaction);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    private void fillBeanFromCursor(Cursor rs, B bean) {
        bean.setCode(rs.getString(rs.getColumnIndex(T.Cols.CODE)));
        bean.setPersonCode(rs.getString(rs.getColumnIndex(T.Cols.PERSON_CODE)));
        bean.setCurCode(rs.getString(rs.getColumnIndex(T.Cols.CUR_CODE)));
        bean.setNotes(rs.getString(rs.getColumnIndex(T.Cols.NOTES)));
        bean.setAmount(rs.getDouble(rs.getColumnIndex(T.Cols.AMOUNT)));
        bean.setType(rs.getInt(rs.getColumnIndex(T.Cols.TYPE)));
        bean.setCreationDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.CREATION_DATE))));
        bean.setTransDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.TRANS_DATE))));
        bean.setCurEqu(rs.getDouble(rs.getColumnIndex(T.Cols.CUR_EQU)));
        bean.setPaymentMethod(rs.getInt(rs.getColumnIndex(T.Cols.PAYMENT_METHOD)));
        bean.setBankCode(rs.getString(rs.getColumnIndex(T.Cols.BANK_CODE)));
        bean.setCheckNum(rs.getString(rs.getColumnIndex(T.Cols.CHECK_NUM)));
        bean.setCheckDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.CHECK_DATE))));
    }

    public int saveBean(B bean)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValuesFromBean(bean, values);

        if(this.getBeanById(bean.getCode()) != null)
        {
            db.update(T.TBL_NAME, values, T.Cols.CODE + " = ?",
                    new String[]{bean.getCode()});
        }
        else
            db.insert(T.TBL_NAME, null, values);

        return 1;
    }

    public B getBeanById(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        B bean = null;
        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , T.Cols.CODE + "=?", new String[]{id+""}, null, null, null);
            if (rs.moveToFirst()) {
                bean = (B)newBean();
                fillBeanFromCursor(rs, bean);
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return bean;
    }

    @Override
    protected String getTableName() {
        return T.TBL_NAME;
    }

    public int updateBean(B bean)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValuesFromBean(bean, values);
        db.update(T.TBL_NAME, values, T.Cols.CODE + " = ?",
                new String[]{bean.getCode()});

        return 1;
    }

    public int saveList(List<B> list, boolean deleteBeforeInsert)
    {
        if (deleteBeforeInsert)
            deleteAll();
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        for (B bean : list) {
            ContentValues values = new ContentValues();
            fillValuesFromBean(bean, values);
            db.insert(T.TBL_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

    private void fillValuesFromBean(B bean, ContentValues values) {
        values.put(T.Cols.CODE, bean.getCode());
        values.put(T.Cols.PERSON_CODE, bean.getPersonCode());
        values.put(T.Cols.CUR_CODE, bean.getCurCode());
        values.put(T.Cols.NOTES, bean.getNotes());
        values.put(T.Cols.TYPE, bean.getType());
        values.put(T.Cols.AMOUNT, bean.getAmount());
        values.put(T.Cols.CREATION_DATE, bean.getCreationDate().getTime());
        values.put(T.Cols.TRANS_DATE, bean.getTransDate().getTime());
        values.put(T.Cols.CUR_EQU,bean.getCurEqu());
        values.put(T.Cols.PAYMENT_METHOD,bean.getPaymentMethod());
        values.put(T.Cols.BANK_CODE,bean.getBankCode());
        values.put(T.Cols.CHECK_NUM,bean.getCheckNum());

        values.put(T.Cols.CHECK_DATE,bean.getCheckDate() != null ? bean.getCheckDate().getTime() : 0);
    }

    public int deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(T.TBL_NAME, null, null);
        return result;
    }

    public boolean deleteBean(String key){
        B bean = getBeanById(key);
        if(bean != null){
            //delete
            String selection = T.Cols.CODE + " = ? ";
            String[] selectionArgs = new String[]{key + ""};
            SQLiteDatabase db = getWritableDatabase();
            db.delete(T.TBL_NAME, selection, selectionArgs);
        }
        return bean != null;
    }

    public void deleteList(List<B> list){
        for (B bean : list){
            deleteBean(bean.getCode());
        }
    }

    public List<B> getTransactionsOfPerson(String personCode,int type){
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String selection=null;
        String[] args = null;

        selection = T.Cols.PERSON_CODE+" =? ";
        args = new String[]{personCode};

        if (type != -1){
            selection = selection + " and " + T.Cols.TYPE+" =?";
            args = new String[]{personCode,type+""};
        }

        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , selection, args , null, null, null);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    B bean = (B)newBean();
                    fillBeanFromCursor(rs, bean);
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    public List<B> getTransactions(String personCode,int type,long fromdate,long todate,int orderby,boolean asc){
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String typeQuery = "";
        if (type != -1){
            typeQuery = " AND " + T.Cols.TYPE + " =" + type;
        }
        String query = "SELECT * FROM " + T.TBL_NAME + " WHERE 1=1";
        if(personCode!= null)
            query = query + " AND " + T.Cols.PERSON_CODE + " = '" + personCode + "' ";
        if(fromdate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " >= '" + fromdate + "' ";
        if(todate > 0)
            query = query + " AND " + T.Cols.TRANS_DATE + " <= '" + todate + "' ";

        query = query + typeQuery;
        if(orderby == 1) {
            query = query + " ORDER BY " + T.Cols.TRANS_DATE;
            if(asc)
                query = query + " ASC";
            else
                query = query + " DESC";
        }
        if(orderby == 2) {
            query = query + " ORDER BY " + T.Cols.AMOUNT;
            if(asc)
                query = query + " ASC";
            else
                query = query + " DESC";
        }

        Cursor rs = null;
        try {
            rs = db.rawQuery(query,null);

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    B bean = (B)newBean();
                    fillBeanFromCursor(rs, bean);
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }
    public double getTotalTransaction(String personCode,int type){
        String sql = "SELECT SUM(" + T.Cols.AMOUNT + "*"+ T.Cols.CUR_EQU +") FROM " + T.TBL_NAME + " WHERE "
                + T.Cols.PERSON_CODE + " = '" + personCode + "' AND " + T.Cols.TYPE + " = " + type;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        return -1;

    }
    public double getSumOfTransaction(int type){
        String sql = "SELECT SUM(" + T.Cols.AMOUNT + "*"+ T.Cols.CUR_EQU +") FROM " + T.TBL_NAME + " WHERE " + T.Cols.TYPE + " = " + type;
        SQLiteDatabase db = null;
        try{
             db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()) {
                double sum = cursor.getDouble(0);
                cursor.close();
                return sum;
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if(db != null)
                db.close();
        }
        return 0;
    }
}
