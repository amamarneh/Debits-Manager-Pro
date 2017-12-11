package amarnehsoft.com.debits.excell;

import android.content.Context;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.sqlite.PersonCatsDB;
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
        return new String[]{mContext.getString(R.string.name),mContext.getString(R.string.phone),mContext.getString(R.string.email),mContext.getString(R.string.address),mContext.getString(R.string.cat)};
    }

    @Override
    protected void writeRecordToSheet(WritableSheet sheet, Person bean, int position) {
        try {
            sheet.addCell(new Label(0,position,bean.getName()));
            sheet.addCell(new Label(1,position,bean.getPhone()));
            sheet.addCell(new Label(2,position,bean.getEmail()));
            sheet.addCell(new Label(3,position,bean.getAddress()));
            PersonCat cat = PersonCatsDB.getInstance(mContext).getBeanById(bean.getCatCode());
            if (cat != null){
                sheet.addCell(new Label(4,position,cat.getName()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
