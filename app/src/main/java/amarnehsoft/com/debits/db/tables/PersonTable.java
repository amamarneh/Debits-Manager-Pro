package amarnehsoft.com.debits.db.tables;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonTable {
    public static final String TBL_NAME = "PERSON_TBL";

    public static final String _CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS "
            + TBL_NAME
            + " ("
            + Cols.KEY + " VARCHAR ,"
            + Cols.NAME + " VARCHAR ,"
            + Cols.PHONE +" VARCHAR ,"
            + Cols.IS_DELETED+" integer ,"
            + Cols.CAT_CODE+" varchar ,"
            + Cols.EMAIL +" VARCHAR ,"
            + Cols.ADDRESS +" VARCHAR "
            + ")";

    public static final class Cols
    {
        public static final String KEY = "TXT_KEY";
        public static final String NAME = "TXT_NAME";
        public static final String PHONE = "TXT_PHONE";
        public static final String EMAIL = "TXT_EMAIL";
        public static final String IS_DELETED="IS_DELETED";
        public static final String CAT_CODE="CAT_CODE";
        public static final String ADDRESS="ADDRESS";
    }
}
