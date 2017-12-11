package amarnehsoft.com.debits.excell;

import android.content.Context;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.constants.PaymentMethod;
import amarnehsoft.com.debits.db.sqlite.BanksDB;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.utils.DateUtils;
import jxl.write.Label;
import jxl.write.WritableSheet;

/**
 * Created by jcc on 12/4/2017.
 */

public class TransactionsExcel extends ExcelHelper<Transaction,TransactionsExcel> {
    private int mType;
    public TransactionsExcel(Context context,int type){
        super(context);
        mType = type;
    }

    @Override
    protected String defaultFileName() {
        return "transactions";
    }

    @Override
    protected String[] defaultTitle() {
        if (mType == 0){
            return new String[]{mContext.getString(R.string.transaction_date),mContext.getString(R.string.person),
                    mContext.getString(R.string.amount),mContext.getString(R.string.currency),mContext.getString(R.string.curEqu),
                    mContext.getString(R.string.paymentMethod),mContext.getString(R.string.notes)};
        }else {
            return new String[]{mContext.getString(R.string.transaction_date),mContext.getString(R.string.person),
                    mContext.getString(R.string.amount),mContext.getString(R.string.currency),mContext.getString(R.string.curEqu),
                    mContext.getString(R.string.paymentMethod),mContext.getString(R.string.notes),
            mContext.getString(R.string.checkNum),mContext.getString(R.string.bank),mContext.getString(R.string.checkDate)};
        }
    }

    @Override
    protected void writeRecordToSheet(WritableSheet sheet, Transaction bean, int position) {
        try {
            sheet.addCell(new Label(0,position, DateUtils.formatDate(bean.getTransDate())));
            Person person = PersonsDB.getInstance(mContext).getBeanById(bean.getPersonCode());
            if (person != null){
                sheet.addCell(new Label(1,position,person.getName()));
            }
            sheet.addCell(new Label(2,position,bean.getAmount()+""));
            Cur cur = CurDB.getInstance(mContext).getBeanById(bean.getCurCode());
            if (cur != null){
                sheet.addCell(new Label(3,position,cur.getName()));
            }
            sheet.addCell(new Label(4,position,bean.getCurEqu()+""));
            PaymentMethod method = PaymentMethod.get(bean.getPaymentMethod());
            if (method != null){
                sheet.addCell(new Label(5,position,method.getString(mContext)));
            }
            sheet.addCell(new Label(6,position,bean.getNotes()));
            if (bean.getType() == 1 && method == PaymentMethod.CHECK){
                sheet.addCell(new Label(7,position,bean.getCheckNum()));
                Bank bank = BanksDB.getInstance(mContext).getBeanById(bean.getBankCode());
                if (bank != null){
                    sheet.addCell(new Label(8,position,bank.getName()));
                }
                sheet.addCell(new Label(9,position,DateUtils.formatDate(bean.getCheckDate())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
