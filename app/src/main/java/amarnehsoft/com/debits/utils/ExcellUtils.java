package amarnehsoft.com.debits.utils;

import android.database.Cursor;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.tables.PersonTable;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by jcc on 12/4/2017.
 */

public class ExcellUtils {
    public void exportToExcel(List list,String fileName,File directory,Locale locale,ExcellListiner listiner) {
        final String fileName2 = fileName + ".xls";

        //Saving file in external storage
//        File sdCard = Environment.getExternalStorageDirectory();
//        File directory = new File(sdCard.getAbsolutePath() + "/javatechig.to do");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName2);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(locale);
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("sheet1", 0);

            try {
                sheet.addCell(new Label(0, 0, "Name")); // column and row
                sheet.addCell(new Label(1, 0, "Phone"));
                List<Person> personList = new ArrayList<>();
                int i=0;
                for (Person person : personList){
                    i++;
                    sheet.addCell(new Label(0,i,person.getName()));
                    sheet.addCell(new Label(1,i,person.getPhone()));
                }
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface ExcellListiner{

    }
}
