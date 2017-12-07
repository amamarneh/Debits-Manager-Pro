package amarnehsoft.com.debits.db.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.DBVersions;
import amarnehsoft.com.debits.db.tables.BanksTable;
import amarnehsoft.com.debits.db.tables.CurTable;
import amarnehsoft.com.debits.db.tables.PersonCatTable;
import amarnehsoft.com.debits.db.tables.PersonTable;
import amarnehsoft.com.debits.db.tables.RemindersTable;
import amarnehsoft.com.debits.db.tables.TransactionsTable;

/**
 * Created by Amarneh on 07/08/2016.
 */
public abstract class DBHelper<T> extends SQLiteOpenHelper
{

    protected Class<T> entityClass;

    protected Context mContext;
    private int mNumberOfItems = 100;

    public static final int VERSION = DBVersions.CURRENT_VERSION.value();
    public static final String DATABASE_NAME = "debits.db";

    protected DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    protected DBHelper(Context context, Class<T> entityClass){
        this(context);
        this.entityClass = entityClass;
    }

    protected T newBean(){
        T bean = null;
        try {
            bean = (T)entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void setNumberOfItems(int numberOfItems){
        mNumberOfItems = numberOfItems;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(PersonTable._CREATE_TABLE);
        db.execSQL(PersonCatTable._CREATE_TABLE);
        db.execSQL(CurTable._CREATE_TABLE);
        db.execSQL(TransactionsTable._CREATE_TABLE);
        db.execSQL(RemindersTable._CREATE_TABLE);
        db.execSQL(BanksTable._CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < DBVersions.Versoin.VERSION_ADD_PAYMENT_METHOD_TO_TRANSACTIONS.value()){
            String sql1 = "alter table " + TransactionsTable.TBL_NAME + " add column " + TransactionsTable.Cols.PAYMENT_METHOD + " integer default(0)";
            String sql2 = "alter table " + TransactionsTable.TBL_NAME + " add column " + TransactionsTable.Cols.BANK_CODE + " varchar ";
            String sql3 = "alter table " + TransactionsTable.TBL_NAME + " add column " + TransactionsTable.Cols.CHECK_NUM + " varchar ";
            String sql4 = "alter table " + TransactionsTable.TBL_NAME + " add column " + TransactionsTable.Cols.CHECK_DATE + " integer ";
            try {
                db.execSQL(sql1);
                db.execSQL(sql2);
                db.execSQL(sql3);
                db.execSQL(sql4);
                db.execSQL(BanksTable._CREATE_TABLE);
                Toast.makeText(mContext,mContext.getString(R.string.upgradingDone),Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.e("Amarneh","exception>>>"+e.getMessage());
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public int getNoOfBeans(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + getTableName();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst())
                count = cursor.getInt(0);

        }finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return count;
    }

    public abstract T getBeanById(String id);
    protected abstract String getTableName();
}