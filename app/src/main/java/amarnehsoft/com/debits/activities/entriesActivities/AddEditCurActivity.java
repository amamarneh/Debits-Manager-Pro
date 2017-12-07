package amarnehsoft.com.debits.activities.entriesActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.utils.NumberUtils;

public class AddEditCurActivity extends AddEditActivity {

    private EditText txtName,txtEqu;
    private Button btnSave;
    private Cur mBean;

    public static Intent getIntent(Context context,String curCode){
        Intent intent = new Intent(context,AddEditCurActivity.class);
        intent.putExtra(ARG_CODE,curCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cur);

        String code =  getIntent().getStringExtra(ARG_CODE);
        if (code != null){
            mBean = CurDB.getInstance(this).getBeanById(code);
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtEqu = (EditText)findViewById(R.id.txtEqu);

        if (mBean != null){
            mMode = MODE_EDIT;
            txtName.setText(mBean.getName());
            txtEqu.setText(mBean.getEqu()+"");
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

        // setting up  defualt cur name
        TextView DefualtCurTextView = (TextView)findViewById(R.id.defualtCurTextView);
        Cur cur = CurDB.getInstance(this).getDefualtBean();
        DefualtCurTextView.setText(cur.getName());

        setupTitle();
    }

    private void setupTitle() {
        switch (mMode){
            case MODE_ADD:
                setTitle(getString(R.string.add_cur));
                break;
            case MODE_EDIT:
                setTitle(getString(R.string.update_cur));
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

        if (TextUtils.isEmpty(txtName.getText())){
            txtName.setError(getString(R.string.not_valid_name));
            return getString(R.string.not_valid_name);
        }
        if (NumberUtils.getDouble(txtEqu.getText().toString()) <= 0){
            txtEqu.setError(getString(R.string.the_equ_must_be_greater_than_zero));
            return getString(R.string.the_equ_must_be_greater_than_zero);
        }
        return null;
    }

    private void save(){
        Cur bean = new Cur();
        if (mMode == MODE_ADD){
            bean.setCode(UUID.randomUUID().toString());
        }else if (mMode == MODE_EDIT){
            bean.setCode(mBean.getCode());
        }

        bean.setName(txtName.getText().toString().trim());
        bean.setEqu(NumberUtils.getDouble(txtEqu.getText().toString()));

        CurDB.getInstance(this).saveBean(bean);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",bean);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

        finish();
    }

    private void showError(String error){
//        ShowSnackbar(error);
    }
    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
