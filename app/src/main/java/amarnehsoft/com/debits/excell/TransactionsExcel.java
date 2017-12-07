package amarnehsoft.com.debits.excell;

import android.content.Context;

import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import jxl.write.Label;
import jxl.write.WritableSheet;

/**
 * Created by jcc on 12/4/2017.
 */

public class TransactionsExcel extends ExcelHelper<Transaction,TransactionsExcel> {

    public TransactionsExcel(Context context){
        super(context);
    }

    @Override
    protected String defaultFileName() {
        return "transactions";
    }

    @Override
    protected String[] defaultTitle() {
        return new String[]{"c1","c2"};
    }

    @Override
    protected void writeRecordToSheet(WritableSheet sheet, Transaction bean, int position) {
        try {
            sheet.addCell(new Label(0,position,bean.getCode()));
            sheet.addCell(new Label(1,position,bean.getPersonCode()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
