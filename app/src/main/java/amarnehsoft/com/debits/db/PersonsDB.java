package amarnehsoft.com.debits.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.tables.PersonTable;
/**
 * Created by Amarneh on 6/3/2017.
 */

public class PersonsDB<B extends Person,T extends PersonTable> extends DBHelper<Person>{

    private static PersonsDB instance;
    public static PersonsDB getInstance(Context context){
        if(instance == null){
            instance = new PersonsDB(context);
        }
        return instance;
    }

    private PersonsDB(Context context) {
        super(context,Person.class);
    }
    public int getNoOfPersonsForCat(String key){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + T.TBL_NAME + " WHERE " + T.Cols.CAT_CODE + " = ?";
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


    public List<B> getAll(int isDeleted,String catCode)
    {

        /*
        isDeleted = -1 ==> getAllPersons
         isDeleted = 0 ==> getNotDeletedPersons
         isDeleted = 1 ==> getDeletedPersons
        */

        SQLiteDatabase db = getReadableDatabase();
        List<B> list = new ArrayList<>();
        String selection=null;
        String[] args = null;
        if(isDeleted==-1){
            selection = null;
            args = null;
        }else if(isDeleted == 0){
            selection = T.Cols.IS_DELETED+ "=?";
            args = new String[]{"0"};
        }else if(isDeleted == 1){
            selection = T.Cols.IS_DELETED+ "=?";
            args = new String[]{"1"};
        }

        if (catCode != null){
            if (selection == null) {
                selection = T.Cols.CAT_CODE+" =?";
                args = new String[]{catCode};
            }else {
                selection = selection + " and "+ T.Cols.CAT_CODE+" =?";
                args = new String[]{isDeleted+"",catCode};
            }
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

    private void fillBeanFromCursor(Cursor rs, B bean) {
        bean.setKey(rs.getString(rs.getColumnIndex(T.Cols.KEY)));
        bean.setName(rs.getString(rs.getColumnIndex(T.Cols.NAME)));
        bean.setPhone(rs.getString(rs.getColumnIndex(T.Cols.PHONE)));
        bean.setEmail(rs.getString(rs.getColumnIndex(T.Cols.EMAIL)));
        bean.setIsDeleted(rs.getInt(rs.getColumnIndex(T.Cols.IS_DELETED)));
        bean.setCatCode(rs.getString(rs.getColumnIndex(T.Cols.CAT_CODE)));
        bean.setAddress(rs.getString(rs.getColumnIndex(T.Cols.ADDRESS)));
    }

    public int saveBean(B bean)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValuesFromBean(bean, values);

        if(this.getBeanById(bean.getKey()) != null)
        {
            db.update(T.TBL_NAME, values, T.Cols.KEY + " = ?",
                    new String[]{bean.getKey()});
        }
        else
            db.insert(T.TBL_NAME, null, values);

        return 1;
    }

    @Override
    public B getBeanById(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        B bean = null;
        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , T.Cols.KEY + "=?", new String[]{id+""}, null, null, null);
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

    public B getBeanByName(String name)
    {
        SQLiteDatabase db = getReadableDatabase();
        B bean = null;
        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , T.Cols.NAME + "=?", new String[]{name+""}, null, null, null);
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
        db.update(T.TBL_NAME, values, T.Cols.KEY + " = ?",
                new String[]{bean.getKey()});

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
        values.put(T.Cols.KEY, bean.getKey());
        values.put(T.Cols.NAME, bean.getName());
        values.put(T.Cols.PHONE, bean.getPhone());
        values.put(T.Cols.EMAIL, bean.getEmail());
        values.put(T.Cols.IS_DELETED,bean.getIsDeleted());
        values.put(T.Cols.CAT_CODE,bean.getCatCode());
        values.put(T.Cols.ADDRESS,bean.getAddress());
    }

    public int deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(T.TBL_NAME, null, null);
        return result;
    }

    public boolean deleteBean(String key){
        B person = getBeanById(key);
        if(person != null){
            if (false){
//            if(isPersonUsedInPayments(key)){
//                //update the flag
//                person.setIsDeleted(1);
//                updateBean(person);
//                return person;
            }else {
                //delete
                String selection = T.Cols.KEY + " = ? ";
                String[] selectionArgs = new String[]{key + ""};
                SQLiteDatabase db = getWritableDatabase();
                db.delete(T.TBL_NAME, selection, selectionArgs);
            }
        }
        return person != null;
    }

    public void deleteList(List<B> list){
        for (B person : list){
            deleteBean(person.getKey());
        }
    }

    public boolean isPersonUsedInPayments(String personId){
        return false;
    }
}
