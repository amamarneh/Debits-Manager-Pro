package amarnehsoft.com.debits.excell;


import android.content.Context;
import android.os.Environment;
import android.os.Parcelable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import amarnehsoft.com.debits.R;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by jcc on 12/5/2017.
 */

public abstract class ExcelHelper<T extends Parcelable,C extends ExcelHelper> {
    //T : bean,C : Child
    //C -> we dont need it till now
    public static final String FOLDER_NAME="/debitsMangerPro.Excel";

    protected Context mContext;
    protected String fileName;
    protected File directory;
    protected Locale locale;
    protected WorkbookSettings wbSettings;
    protected String[] title;
    protected String sheetName;


    public ExcelHelper(Context context){
        mContext = context;
    }


    public C setFileName(String fileName){
        this.fileName = fileName + ".xls";
        return (C)this;
    }

    public C setDirectory(File directory){
        this.directory = directory;
        return (C)this;
    }

    public C setLocale(Locale locale){
        this.locale = locale;
        return (C)this;
    }

    public boolean generate(List<T> list){
        if (fileName == null){
            fileName =defaultFileName();
        }

        if (sheetName == null){
            sheetName = mContext.getString(R.string.sheet);
        }

        //Saving file in external storage
        if (directory == null){
            File sdCard = Environment.getExternalStorageDirectory();
            directory = new File(sdCard.getAbsolutePath() + FOLDER_NAME);
        }
        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);
        wbSettings = new WorkbookSettings();

        if (locale == null){
            locale = Locale.ENGLISH;
        }

        wbSettings.setLocale(locale);

        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(sheetName, 0);

            if (title == null){
                title = defaultTitle();
            }

            writeContentToSheet(sheet,list);

            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void writeContentToSheet(WritableSheet sheet,List<T> list) {
        try {
            int i=0;
            for (String t : this.title){
                sheet.addCell(new Label(i, 0, t));
                i++;
            }

            i=0;
            for (T bean : list){
                i++;
                writeRecordToSheet(sheet,bean,i);
            }

        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    protected abstract String defaultFileName();
    protected abstract String[] defaultTitle();
    protected abstract void writeRecordToSheet(WritableSheet sheet,T bean,int position);
}
