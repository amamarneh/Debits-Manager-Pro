package amarnehsoft.com.debits.fragments.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.controllers.LanguageController;
import amarnehsoft.com.debits.excell.ExcelHelper;
import amarnehsoft.com.debits.excell.PersonsExcel;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.excell.TransactionsExcel;

/**
 * Created by jcc on 8/22/2017.
 */

public class GenerateExcelDialogFragment extends DialogFragment {
    public static final String TAG="GenerateExcelDialogFragment";

    private static final String ARG_FILE_NAME="fileNAme";
    private static final String ARG_LIST="list";

    private OnFragmentInteractionListener mListener;

    public static DialogFragment newInstance(ArrayList<Parcelable> list, String defaultFileName){
        DialogFragment fragment = new GenerateExcelDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_NAME,defaultFileName);
        args.putParcelableArrayList(ARG_LIST,list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_generate_excel, container, false);
        TextInputEditText txtFileName = (TextInputEditText)v.findViewById(R.id.txtFileName);
        Button btnGenerate = (Button)v.findViewById(R.id.btnGenerate);
        TextView txtFolderName = (TextView)v.findViewById(R.id.txtFolderName);

        txtFolderName.setText(ExcelHelper.FOLDER_NAME);

        String defaultFileName = getArguments().getString(ARG_FILE_NAME);
        if (defaultFileName == null) defaultFileName ="";
        txtFileName.setText(defaultFileName);

        final List list = getArguments().getParcelableArrayList(ARG_LIST);
        final String finalDefaultFileName = defaultFileName;
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list == null || list.isEmpty()){
                    Toast.makeText(getContext(),getString(R.string.no_data_to_generate),Toast.LENGTH_LONG).show();
                }else {
                    Object bean = list.get(0);
                    if (bean instanceof Person){
                        if(new PersonsExcel(getContext()).setFileName(finalDefaultFileName).generate((List<Person>)list)){
                            mListener.exportedToExcelSuccessfully();
                        }else {
                            mListener.errorWhileExportingToExcel();
                        }
                        dismiss();
                    }else if(bean instanceof Transaction){
                        int type = ((Transaction) bean).getType();
                        if (new TransactionsExcel(getContext(),type).setFileName(finalDefaultFileName).generate((List<Transaction>)list)){
                            mListener.exportedToExcelSuccessfully();
                        }else {
                            mListener.errorWhileExportingToExcel();
                        }
                        dismiss();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener)context;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener{
        void exportedToExcelSuccessfully();
        void errorWhileExportingToExcel();
    }
}