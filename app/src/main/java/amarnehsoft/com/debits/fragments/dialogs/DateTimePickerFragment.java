package amarnehsoft.com.debits.fragments.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alaam on 12/20/2017.
 */

public class DateTimePickerFragment extends DialogFragment {

    public static final String ARG_DATE = "calendar";
    public static final String ARG_REQ_CODE = "reqCode";

    private int mReqCode = -1;
    private IDateTimePickerListener mListener;

    public void setmListener(IDateTimePickerListener mListener) {
        this.mListener = mListener;
    }


    public static DateTimePickerFragment newInstance(Date date, int requestCode, IDateTimePickerListener mListener) {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setmListener(mListener);
        Bundle bundle = new Bundle();
        if(date == null)
        bundle.putLong(ARG_DATE,0);
        else
            bundle.putLong(ARG_DATE,date.getTime());

        bundle.putInt(ARG_REQ_CODE, requestCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    public void Show(final Context context){
//        final long c = getArguments().getLong(ARG_DATE);
//        Date date = new Date();
//        if(c != 0)
//            date.setTime(c);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date1 = calendar.getTime();
                showTimePicker(date1,context);
            }
        },year,month,day);

        datePickerDialog.show();

    }

    private void showTimePicker(final Date date1,Context context) {
        long c = getArguments().getLong(ARG_DATE);
        Date date = new Date();
        if(c != 0)
            date.setTime(c);
        final int r = getArguments().getInt(ARG_REQ_CODE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                date1.setHours(i);
                date1.setMinutes(i1);
                mListener.onDateTimeSet(date1,r);
            }
        },date.getHours(),date.getMinutes(),false);

        timePickerDialog.show();
    }

    public interface IDateTimePickerListener{
        void onDateTimeSet(Date date,int reqCode);
    }
}
