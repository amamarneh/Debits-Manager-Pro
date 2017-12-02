package amarnehsoft.com.debits.activities.entriesActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.BanksDB;
import amarnehsoft.com.debits.db.PersonCatsDB;

public class AddEditBankActivity extends AddEditActivity {

    private EditText txtName;
    private Button btnSave;

    private Bank mBean;

    public static Intent getIntent(Context context,String bankCode){
        Intent intent = new Intent(context,AddEditBankActivity.class);
        intent.putExtra(ARG_CODE,bankCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_bank);

        String code =  getIntent().getStringExtra(ARG_CODE);
        if (code != null){
            mBean = BanksDB.getInstance(this).getBeanById(code);
        }

        txtName = (EditText) findViewById(R.id.txtName);

        if (mBean != null){
            mMode = MODE_EDIT;
            String name = null;
            if (mBean != null)
                name = mBean.getName();
            else
                name = getString(R.string.not_found);
            txtName.setText(name);
        }else{
            mMode = MODE_ADD;
        }

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCommand();
            }
        });

        setupTitle();
    }
    private void setupTitle() {
        switch (mMode){
            case MODE_ADD:
                setTitle(getString(R.string.addBank));
                break;
            case MODE_EDIT:
                setTitle(getString(R.string.updateBank));
        }
    }

    private void saveCommand(){
        String validation = validate();
        if (validation == null){
            save();
        }else {
            showError(validation);
        }
    }

    private String validate(){
        if (txtName.getText().toString().isEmpty()){
            return getString(R.string.not_valid_name);
        }
        return null;
    }

    private void save(){
        Bank bean = new Bank();
        if (mMode == MODE_ADD){
            bean.setCode(UUID.randomUUID().toString());
        }else if (mMode == MODE_EDIT){
            if (mBean != null)
                bean.setCode(mBean.getCode());
        }

        bean.setName(txtName.getText().toString().trim());

        BanksDB.getInstance(this).saveBean(bean);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",bean);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void showError(String error){
        txtName.setError(error);
//        ShowSnackbar(error);
    }
    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
