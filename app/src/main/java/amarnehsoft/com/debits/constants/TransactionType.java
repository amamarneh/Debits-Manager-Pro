package amarnehsoft.com.debits.constants;

/**
 * Created by jcc on 12/3/2017.
 */

public enum TransactionType {
    DEBIT(0),PAYMENT(1);
    private int type;
    TransactionType(int type){
        this.type = type;
    }
    public int value(){
        return type;
    }
}
