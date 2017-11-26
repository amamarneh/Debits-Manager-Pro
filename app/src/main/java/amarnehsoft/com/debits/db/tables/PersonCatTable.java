package amarnehsoft.com.debits.db.tables;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonCatTable {
    public static final String TBL_NAME = "PERSON_CAt_TBL";

    public static final String _CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS "
            + TBL_NAME
            + " ("
            + Cols.CODE + " VARCHAR ,"
            + Cols.NAME + " VARCHAR "
            + ")";

    public static final class Cols
    {
        public static final String CODE = "TXT_KEY";
        public static final String NAME = "TXT_NAME";
    }
}
