package amarnehsoft.com.debits.db;

/**
 * Created by Amarneh on 12/1/2017.
 */

public class DBVersions {
    public enum Versoin{
        VERSION_ADD_PAYMENT_METHOD_TO_TRANSACTIONS(8);
        private int version;
        Versoin(int version){
            this.version=version;
        }
        public int value(){return version;}
    }

    public static final Versoin CURRENT_VERSION = Versoin.VERSION_ADD_PAYMENT_METHOD_TO_TRANSACTIONS;
}
