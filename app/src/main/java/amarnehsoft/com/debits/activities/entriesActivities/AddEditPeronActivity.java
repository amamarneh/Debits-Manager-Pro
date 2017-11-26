package amarnehsoft.com.debits.activities.entriesActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.listActivities.ListActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsCatListActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.PersonCatsDB;
import amarnehsoft.com.debits.db.PersonsDB;

public class AddEditPeronActivity extends AddEditActivity {

    public static final int REQ_CAT = 1;

    private EditText txtName;
    private EditText txtPhone,txtEmail,txtAddress;
    private TextView txtCat;
    private Button btnSave;

    private PersonCat selectedCat;

    private Person mBean;

    public static Intent getIntent(Context context,String personCode){
        Intent intent = new Intent(context,AddEditPeronActivity.class);
        intent.putExtra(ARG_CODE,personCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_peron);

        String code =  getIntent().getStringExtra(ARG_CODE);
        if (code != null){
            mBean = PersonsDB.getInstance(this).getBeanById(code);
        }

        txtName = (EditText)findViewById(R.id.txtName);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtCat = (TextView)findViewById(R.id.txtCat);
        txtAddress   = (EditText)findViewById(R.id.txtAddress);

        if (mBean != null){
            mMode = MODE_EDIT;
            txtName.setText(mBean.getName());
            txtEmail.setText(mBean.getEmail());
            txtPhone.setText(mBean.getPhone());
            selectedCat = PersonCatsDB.getInstance(this).getBeanById(mBean.getCatCode());
            txtCat.setText(selectedCat.getName());
            txtAddress.setText(mBean.getAddress());
        }else{
            mMode = MODE_ADD;
            selectedCat = PersonCatsDB.getInstance(this).getDefualtBean();
            String catName = null;
            if (selectedCat != null)
                catName = selectedCat.getName();
            else
                catName = getString(R.string.not_found);
            txtCat.setText(catName);
        }

        txtCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PersonsCatListActivity.getIntent(AddEditPeronActivity.this,1, ListActivity.MODE_SEARCH),REQ_CAT);
            }
        });

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
                setTitle(getString(R.string.add_person));
                break;
            case MODE_EDIT:
                setTitle(getString(R.string.update_person));
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
        Person bean = new Person();
        if (mMode == MODE_ADD){
            bean.setKey(UUID.randomUUID().toString());
        }else if (mMode == MODE_EDIT){
            bean.setKey(mBean.getKey());
        }

        bean.setName(txtName.getText().toString().trim());
        bean.setPhone(txtPhone.getText().toString());
        bean.setEmail(txtEmail.getText().toString().trim());
        if (selectedCat != null)
            bean.setCatCode(selectedCat.getCode());
        bean.setAddress(txtAddress.getText().toString());
        bean.setIsDeleted(0);

        PersonsDB.getInstance(this).saveBean(bean);
        //showSuccess();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAT){
            if (resultCode == RESULT_OK){
                selectedCat = data.getParcelableExtra("data");
                if (selectedCat != null)
                    txtCat.setText(selectedCat.getName());
            }
        }
    }

    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
