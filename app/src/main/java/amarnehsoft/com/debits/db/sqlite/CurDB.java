package amarnehsoft.com.debits.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.db.tables.CurTable;
import amarnehsoft.com.debits.db.tables.PersonTable;
import amarnehsoft.com.debits.utils.Defualts;

/**
 * Created by Amarneh on 6/3/2017.
 */

public class CurDB<B extends Cur,T extends CurTable> extends DBHelper<Cur> {
    //b : bean type ,, T: table type
    private static CurDB instance;
    public static CurDB getInstance(Context context){
        if(instance == null){
            instance = new CurDB<Cur,CurTable>(context);
        }
        return instance;
    }

    private CurDB(Context context) {
        super(context,Cur.class);
    }

    public List<B> getAll()
    {
        int isDeleted = -1;
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String selection=null;
        String[] args = null;
        if(isDeleted == -1){
            selection = null;
            args = null;
        }else if(isDeleted == 0){
            selection = PersonTable.Cols.IS_DELETED+ "=?";
            args = new String[]{"0"};
        }else if(isDeleted == 1){
            selection = PersonTable.Cols.IS_DELETED+ "=?";
            args = new String[]{"1"};
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

    public List<B> getSearchQueryResult(String query)
    {
        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();

        Cursor rs = null;
        try {
            rs = db.rawQuery("SELECT * FROM " + T.TBL_NAME +
                            " WHERE " + T.Cols.NAME + " LIKE '%" + query + "%' "
                    , null);

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

    private void fillBeanFromCursor(Cursor rs, Cur bean) {
        bean.setCode(rs.getString(rs.getColumnIndex(T.Cols.CODE)));
        bean.setName(rs.getString(rs.getColumnIndex(T.Cols.NAME)));
        bean.setEqu(rs.getDouble(rs.getColumnIndex(T.Cols.EQU)));
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
        values.put(T.Cols.NAME, bean.getName());
        values.put(T.Cols.EQU , bean.getEqu());
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

    public B getDefualtBean(){
        B bean = getBeanById(Defualts.DEFUALT);
        if(bean == null){
            bean = (B)newBean();
            bean.setCode(Defualts.DEFUALT);
            bean.setName(Defualts.curName(mContext));
            bean.setEqu(1);
            saveBean(bean);
        }
        return bean;
    }

}
