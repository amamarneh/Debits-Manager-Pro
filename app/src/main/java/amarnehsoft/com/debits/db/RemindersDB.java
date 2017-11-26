package amarnehsoft.com.debits.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.tables.PersonTable;
import amarnehsoft.com.debits.db.tables.RemindersTable;
import amarnehsoft.com.debits.db.tables.TransactionsTable;

/**
 * Created by Amarneh on 6/3/2017.
 */

public class RemindersDB<B extends Reminder,T extends RemindersTable> extends DBHelper<Reminder>{
    //b : bean type ,, T: table type

    private static RemindersDB instance;
    public static RemindersDB getInstance(Context context){
        if(instance == null){
            instance = new RemindersDB<Reminder,RemindersTable>(context);
        }
        return instance;
    }

    private RemindersDB(Context context) {
        super(context,Reminder.class);
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

    private void fillBeanFromCursor(Cursor rs, B bean) {
        bean.setCode(rs.getString(rs.getColumnIndex(T.Cols.CODE)));
        bean.setPersonCode(rs.getString(rs.getColumnIndex(T.Cols.PERSON_CODE)));
        bean.setCurCode(rs.getString(rs.getColumnIndex(T.Cols.CUR_CODE)));
        bean.setNotes(rs.getString(rs.getColumnIndex(T.Cols.NOTES)));
        bean.setAmount(rs.getDouble(rs.getColumnIndex(T.Cols.AMOUNT)));
        bean.setType(rs.getInt(rs.getColumnIndex(T.Cols.TYPE)));
        bean.setCreationDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.CREATION_DATE))));
        bean.setTransDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.TRANS_DATE))));
        bean.setReminerDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.REMINDER_DATE))));
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
        values.put(T.Cols.REMINDER_DATE,bean.getReminerDate().getTime());
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

    public List<B> getSearchQueryResult(int type,String query)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();

        String typeQuery = "";
        if (type != -1){
            typeQuery = " AND " + T.Cols.TYPE + " =" + type;
        }

        String sql = "SELECT * FROM " + T.TBL_NAME  + ", " + PersonTable.TBL_NAME +
                " WHERE " + T.TBL_NAME + "." + T.Cols.PERSON_CODE + " = " + PersonTable.TBL_NAME+ "." + PersonTable.Cols.KEY + " and " + PersonTable.TBL_NAME+"." + PersonTable.Cols.NAME  + " LIKE '%"+ query + "%'" + typeQuery +
                " GROUP BY " + T.TBL_NAME+"." + T.Cols.CODE + " ORDER BY " + PersonTable.TBL_NAME+"."+PersonTable.Cols.NAME;

        Cursor rs = null;
        try {
            rs = db.rawQuery(sql, null);

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

    public List<B> getRemindersOfPerson(String personCode,int type){
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

}
