package amarnehsoft.com.debits.fragments.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.controllers.LanguageController;

/**
 * Created by jcc on 8/22/2017.
 */

public class LanguagesDialogFragment extends DialogFragment {
    public static final String TAG="languageDialogFragment";

    public static DialogFragment newInstance(){
        DialogFragment fragment = new LanguagesDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_languages, container, false);
        Button btnArabic = (Button) v.findViewById(R.id.btnArabic);
        Button btnEnglish = (Button)v.findViewById(R.id.btnEnglish);

        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageController.arabic(getContext());
                getActivity().recreate();
                dismiss();
            }
        });


        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageController.english(getContext());
                getActivity().recreate();
                dismiss();
            }
        });

        return v;
    }
}