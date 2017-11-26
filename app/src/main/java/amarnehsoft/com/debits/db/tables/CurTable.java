package amarnehsoft.com.debits.db.tables;

/**
 * Created by jcc on 8/18/2017.
 */

public class CurTable {
    public static final String TBL_NAME = "CUR_TBL";

    public static final String _CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS "
            + TBL_NAME
            + " ("
            + Cols.CODE + " VARCHAR ,"
            + Cols.NAME + " VARCHAR ,"
            + Cols.EQU + " real "
            + ")";

    public static final class Cols
    {
        public static final String CODE = "TXT_KEY";
        public static final String NAME = "TXT_NAME";
        public static final String EQU = "TXT_EQU"; // equevelent
    }
}
