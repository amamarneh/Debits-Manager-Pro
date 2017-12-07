package amarnehsoft.com.debits.excell;

import android.content.Context;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import jxl.write.Label;
import jxl.write.WritableSheet;

/**
 * Created by jcc on 12/4/2017.
 */

public class PersonsExcel extends ExcelHelper<Person,PersonsExcel> {

    public PersonsExcel(Context context){
        super(context);
    }

    @Override
    protected String defaultFileName() {
        return "persons";
    }

    @Override
    protected String[] defaultTitle() {
        return new String[]{mContext.getString(R.string.name),mContext.getString(R.string.phone)};
    }

    @Override
    protected void writeRecordToSheet(WritableSheet sheet, Person bean, int position) {
        try {
            sheet.addCell(new Label(0,position,bean.getName()));
            sheet.addCell(new Label(1,position,bean.getPhone()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
