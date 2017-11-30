package amarnehsoft.com.debits.activities.entriesActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.CalculatorActivity;
import amarnehsoft.com.debits.activities.listActivities.CurListActivity;
import amarnehsoft.com.debits.activities.listActivities.ListActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.controllers.SPController;
import amarnehsoft.com.debits.db.BalancesDB;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.TransactionsDB;
import amarnehsoft.com.debits.fragments.dialogs.DatePickerFragment;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.Defualts;
import amarnehsoft.com.debits.utils.MyColors;
import amarnehsoft.com.debits.utils.NumberUtils;

public class AddEditTransactionActivity extends AddEditActivity implements DatePickerFragment.IDatePickerFragment{

    public static final int REQ_SELECT_PERSON = 1;
    public static final int REQ_SELECT_CUR=2;
    public static final int REQ_CALCULATOR=3;

    private final int TYPE_DEBIT = 0;
    private final int TYPE_PAYMENT = 1;

    private AutoCompleteTextView txtName;
    private EditText txtAmount, txtNotes,txtCurEqu;
    private TextView txtBalance;
    private Button txtCur,txtTransDate,btnSave,btnChoosePerson;
    private RadioButton meRadioBtn,onMeRadioBtn;
    private View btnCalculator,balanceLayout;
    private Transaction mBean;
    private Cur selectedCur;
    private Date selectedTransDate;
    private Person selectedPerson;
    private int selectedType;
//    private int mode;

    public static Intent getIntent(Context context, String transCode, @Nullable HashMap<String,Object> map){
        Intent intent = new Intent(context,AddEditTransactionActivity.class);
        intent.putExtra(ARG_CODE,transCode);
        intent.putExtra("map",map);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_transaction);


        String code =  getIntent().getStringExtra(ARG_CODE);

        if (code != null){
            mBean = TransactionsDB.getInstance(this).getBeanById(code);
        }

        txtName = (AutoCompleteTextView) findViewById(R.id.txtName);
        setupAutoComplete();
        txtAmount = (EditText)findViewById(R.id.txtAmount);
        txtCur = (Button)findViewById(R.id.txtCur);
        txtCurEqu  =(EditText)findViewById(R.id.txtCurEqu);
        txtNotes = (EditText)findViewById(R.id.txtNotes);
        txtTransDate = (Button)findViewById(R.id.txtTransDate);
        meRadioBtn = (RadioButton)findViewById(R.id.meRadioBtn);
        onMeRadioBtn = (RadioButton)findViewById(R.id.onMeRadioBtn);
        btnCalculator=findViewById(R.id.btnCalc);
        txtBalance = (TextView)findViewById(R.id.txtBalance);
        balanceLayout = findViewById(R.id.balanceLayout);

        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CalculatorActivity.newIntent(AddEditTransactionActivity.this,null,txtAmount.getText().toString()),REQ_CALCULATOR);
            }
        });

        meRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedType = 0;
                    setupTitle(selectedType);
                }
            }
        });

        onMeRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedType = 1;
                    setupTitle(selectedType);
                }
            }
        });

        btnChoosePerson = (Button)findViewById(R.id.btnChoosePerson);
        btnChoosePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PersonsActivity.getIntent(AddEditTransactionActivity.this,1, ListActivity.MODE_SEARCH),REQ_SELECT_PERSON);
            }
        });

        txtCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CurListActivity.getIntent(AddEditTransactionActivity.this,1,ListActivity.MODE_SEARCH),REQ_SELECT_CUR);
            }
        });
        btnSave = (Button)findViewById(R.id.btnSave);

        txtTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.newInstance(getCalendar(),0).show(getSupportFragmentManager(),"datePicker");
            }
        });

        if (mBean != null){
            mMode = MODE_EDIT;
            selectedPerson = PersonsDB.getInstance(this).getBeanById(mBean.getPersonCode());
            String personName = "";
            if (selectedPerson != null) personName = selectedPerson.getName();
            txtName.setText(personName);
            selectedTransDate = mBean.getTransDate();
            txtTransDate.setText(getString(R.string.transactionDate) + "\n" + formatDate(selectedTransDate));
            txtNotes.setText(mBean.getNotes());
            selectedCur = CurDB.getInstance(this).getBeanById(mBean.getCurCode());
            String curName = getString(R.string.not_found);
            if (selectedCur != null){
                curName = selectedCur.getName();
//                double curEqu = selectedCur.getEqu();
                double curEqu = mBean.getCurEqu();
                txtCurEqu.setText(curEqu+"");
                // setup CurEqu layout
                Cur defaultCur = CurDB.getInstance(this).getDefualtBean();
                if(defaultCur.getName().equals(selectedCur.getName()))
                    showCurEquLayout(false);
                else
                    showCurEquLayout(true);
            }
            txtCur.setText(curName);
            txtAmount.setText(mBean.getAmount()+"");
            selectedType = mBean.getType();
            if (selectedType == 0){
                meRadioBtn.setChecked(true);
            }else {
                onMeRadioBtn.setChecked(true);
            }
        }else{
            mMode = MODE_ADD;
            showCurEquLayout(false);
            selectedCur = CurDB.getInstance(this).getDefualtBean();
            String curName = getString(R.string.not_found);
            if (selectedCur != null){
                curName = selectedCur.getName();
                txtCurEqu.setText(selectedCur.getEqu()+"");
            }
            txtCur.setText(curName);
            selectedTransDate = new Date();
            txtTransDate.setText(getString(R.string.transactionDate) + "\n" +  formatDate(selectedTransDate));
            selectedType = 0;
            meRadioBtn.setChecked(true);
            HandlingMap();
        }


        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSaveClick();
            }
        });
        //
        setupTitle(selectedType);
        //txtAmount.requestFocus();
    }

    private void HandlingMap() {
        HashMap<String,Object> map = (HashMap<String, Object>)getIntent().getSerializableExtra("map");
        if(map != null)
        {
            Person person = null;
            int type = TYPE_DEBIT;
            if(map.get("person") != null) {
                person = (Person) map.get("person");
                selectedPerson = person;
                txtName.setText(selectedPerson.getName());
            }

            if(map.get("type") != null)
                type = (Integer)map.get("type");
            if(type == TYPE_PAYMENT) {
                selectedType = TYPE_PAYMENT;
                setupTitle(selectedType);
                onMeRadioBtn.setChecked(true);
            }else{
                selectedType = TYPE_DEBIT;
                setupTitle(selectedType);
                meRadioBtn.setChecked(true);
            }
        }
    }

    private Calendar getCalendar(){
        return DateUtils.getCalendarFromDate(selectedTransDate);
    }

    private void setupTitle(int selectedType) {

        if(mMode == MODE_ADD) {
            if (selectedType == TYPE_DEBIT)
                setTitle(getString(R.string.add_debit));
            else if (selectedType == TYPE_PAYMENT)
                setTitle(getString(R.string.add_payment));
        }else
            if(mMode == MODE_EDIT){
                if(selectedType == TYPE_DEBIT)
                    setTitle(getString(R.string.update_debit));
                else
                if(selectedType == TYPE_PAYMENT)
                    setTitle(getString(R.string.update_payment));
            }

        ActionBar mActionBar = getSupportActionBar();
        if(selectedType == TYPE_DEBIT) {
            if (mActionBar != null)
                mActionBar.setBackgroundDrawable(new ColorDrawable(MyColors.debitColor));
        }else if(selectedType == TYPE_PAYMENT){
            if (mActionBar != null)
                mActionBar.setBackgroundDrawable(new ColorDrawable(MyColors.paymentColor));
        }
    }

    private void setupAutoComplete(){
        txtName.setThreshold(1); //search from first char
        List<Person> persons = PersonsDB.getInstance(this).getAll(-1,null);
        String[] arrPersons = new String[persons.size()];
        for (int i=0 ; i<persons.size() ; i++){
            arrPersons[i] = persons.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrPersons);

        txtName.setAdapter(adapter);

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Person person = PersonsDB.getInstance(AddEditTransactionActivity.this).getBeanByName(s.toString());
                if (person != null){
                    showBalanceForPerson(person);
                }else {
                    hideBalanceLayout();
                }
            }
        });
    }

    private void showBalanceForPerson(Person person){
        balanceLayout.setVisibility(View.VISIBLE);
        double balance = BalancesController.getTotalBalance(this,person.getKey());
        if (balance == 0){
            txtBalance.setText("0.0");
        }else if (balance > 0){
            txtBalance.setTextColor(getResources().getColor(R.color.debitColor));
            txtBalance.setText(balance + " " + SPController.newInstance(this).getBaseCur().getName()+" " + getString(R.string.debits));
        }else {
            //balance < 0
            balance=-balance;
            txtBalance.setTextColor(getResources().getColor(R.color.paymentColor));
            txtBalance.setText(balance + " " + SPController.newInstance(this).getBaseCur().getName()+" " + getString(R.string.credits));
        }
    }

    private void hideBalanceLayout(){
        balanceLayout.setVisibility(View.GONE);
    }

    private String formatDate(Date date){
        return DateUtils.formatDateWithoutTime(date);
    }

    private void saveCommand(){
        String validation = validate();
        if (validation == null){
            TrySave();
        }else {
//            showError(validation);
        }
    }

    private String validate(){
        if (txtName.getText().toString().isEmpty()){
            txtName.setError(getString(R.string.not_valid_name));
            return getString(R.string.not_valid_name);
        }
        if (!(NumberUtils.getDouble(txtAmount.getText().toString()) > 0)){
            txtAmount.setError(getString(R.string.amount_field_not_valid));
            return getString(R.string.amount_field_not_valid);
        }
        return null;
    }
    private void TrySave(){
        Person person = PersonsDB.getInstance(this).getBeanByName(txtName.getText().toString().trim());
        if(person == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.unknounPerson))
                    .setMessage(getString(R.string.thisPersonDoesntExistAddIt))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Person person = new Person();
                            person.setKey(UUID.randomUUID().toString());
                            person.setName(txtName.getText().toString().trim());
                            person.setPhone("");
                            person.setEmail("");
                            person.setCatCode(Defualts.DEFUALT);
                            person.setIsDeleted(0);

                            PersonsDB.getInstance(AddEditTransactionActivity.this).saveBean(person);

                            save(person);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else{
            save(person);
        }
    }

    private void save(Person person){

        Transaction bean = new Transaction();
        if (mMode == MODE_ADD){
            bean.setCode(UUID.randomUUID().toString());
        }else if (mMode == MODE_EDIT){
            bean.setCode(mBean.getCode());
        }
        if (person != null)
            bean.setPersonCode(person.getKey());
        bean.setNotes(txtNotes.getText().toString());
        if (selectedCur != null){
            bean.setCurCode(selectedCur.getCode());
            bean.setCurEqu(NumberUtils.getDouble(txtCurEqu.getText().toString()));
        }

        bean.setAmount(NumberUtils.getDouble(txtAmount.getText().toString()));
        bean.setCreationDate(new Date());
        bean.setTransDate(selectedTransDate);
        bean.setType(selectedType);

        TransactionsDB.getInstance(AddEditTransactionActivity.this).saveBean(bean);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",bean);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        Log.d("Amarneh","edit ,, bean.getCode="+bean.getCode());
        finish();

    }

    private void showError(String error){
        ShowSnackbar(error);
    }

    private Person savePersonIfNotExist(String personName){
        Person person = PersonsDB.getInstance(this).getBeanByName(personName);
        if (person == null){
            person = new Person();
            person.setKey(UUID.randomUUID().toString());
            person.setName(personName);
            person.setPhone("");
            person.setEmail("");
            person.setCatCode(Defualts.DEFUALT);
            person.setIsDeleted(0);





            PersonsDB.getInstance(this).saveBean(person);
        }
        return person;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Amarneh","onActivityResult");
        if (resultCode == RESULT_OK){
            if (requestCode == REQ_SELECT_PERSON){
                selectedPerson = data.getParcelableExtra("data");
                if (selectedPerson != null)
                    txtName.setText(selectedPerson.getName(),false);
                txtAmount.requestFocus();
                txtAmount.setFocusable(true);
            }

            if (requestCode == REQ_SELECT_CUR){
                selectedCur = data.getParcelableExtra("data");
                if (selectedCur != null) {
                    txtCur.setText(selectedCur.getName());
                    txtCurEqu.setText(selectedCur.getEqu() + "");
                    Cur defaultCur = CurDB.getInstance(this).getDefualtBean();
                    if(defaultCur.getName().equals(selectedCur.getName()))
                        showCurEquLayout(false);
                    else
                        showCurEquLayout(true);
                }
            }

            if (requestCode == REQ_CALCULATOR){
                double result = data.getDoubleExtra(CalculatorActivity.ARG_RESULT,0.0);
                txtAmount.setText(result + "");
            }
        }
    }

    private void showCurEquLayout(boolean b) {
        View layout = findViewById(R.id.layoutCurEqu);
        layout.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public void onDateSet(int reqCode,int year, int month, int day) {
        selectedTransDate = DateUtils.getDate(year,month,day);
        txtTransDate.setText(getString(R.string.transactionDate) + "\n" + formatDate(selectedTransDate));
    }

    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
