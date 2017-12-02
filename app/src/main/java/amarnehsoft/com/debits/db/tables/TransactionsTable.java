package amarnehsoft.com.debits.db.tables;

/**
 * Created by jcc on 8/18/2017.
 */

public class TransactionsTable {
    public static final String TBL_NAME = "transactions_TBL";

    public static final String _CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS "
            + TBL_NAME
            + " ("
            + Cols.CODE + " VARCHAR ,"
            + Cols.NOTES + " VARCHAR ,"
            + Cols.PERSON_CODE + " VARCHAR ,"
            + Cols.CUR_CODE + " VARCHAR ,"
            + Cols.TYPE + " INTEGER ,"
            + Cols.AMOUNT + " real ,"
            + Cols.CREATION_DATE + " INTEGER ,"
            + Cols.PAYMENT_METHOD + " INTEGER ,"
            + Cols.BANK_CODE + " VARCHAR ,"
            + Cols.CHECK_NUM + " VARCHAR ,"
            + Cols.CHECK_DATE + " INTEGER ,"
            + Cols.CUR_EQU + " INTEGER ,"
            + Cols.TRANS_DATE + " INTEGER "
            + ")";


    public static final class Cols
    {
        public static final String CODE = "TXT_KEY";
        public static final String TYPE = "INT_TYPE";
        public static final String PERSON_CODE = "TXT_PERSON_CODE";
        public static final String CUR_CODE = "TXT_CUR_CODE";
        public static final String AMOUNT = "DBL_AMOUNT";
        public static final String NOTES = "TXT_NOTES";
        public static final String CREATION_DATE = "LNG_CREATION_DATE";
        public static final String TRANS_DATE = "LNG_TRANS_DATE";
        public static final String CUR_EQU="DBL_CUR_EQU";
        public static final String PAYMENT_METHOD="INT_PAYMENT_METHOD";
        public static final String BANK_CODE="TXT_BANK_CODE";
        public static final String CHECK_NUM="TXT_CHECK_NUM";
        public static final String CHECK_DATE="LNG_CHECK_DATE";
    }
}
