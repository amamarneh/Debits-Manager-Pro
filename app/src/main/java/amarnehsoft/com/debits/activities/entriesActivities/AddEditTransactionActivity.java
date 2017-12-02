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

import java.io.BufferedReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.CalculatorActivity;
import amarnehsoft.com.debits.activities.listActivities.BanksListActivity;
import amarnehsoft.com.debits.activities.listActivities.CurListActivity;
import amarnehsoft.com.debits.activities.listActivities.ListActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsActivity;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.constants.PaymentMethod;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.controllers.SPController;
import amarnehsoft.com.debits.db.BanksDB;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.TransactionsDB;
import amarnehsoft.com.debits.fragments.dialogs.DatePickerFragment;
import amarnehsoft.com.debits.popup.PaymentMethodPopup;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.Defualts;
import amarnehsoft.com.debits.utils.MyColors;
import amarnehsoft.com.debits.utils.NumberUtils;

public class AddEditTransactionActivity extends AddEditActivity implements DatePickerFragment.IDatePickerFragment{

    public static final int REQ_SELECT_PERSON = 1;
    public static final int REQ_SELECT_CUR=2;
    public static final int REQ_CALCULATOR=3;
    private static final int REQ_SELECT_BANK=4;
    private static final int REQ_TRANS_DATE=5;
    private static final int REQ_CHECK_DATE=6;

    private final int TYPE_DEBIT = 0;
    private final int TYPE_PAYMENT = 1;

    private AutoCompleteTextView txtName,txtBank;
    private EditText txtAmount, txtNotes,txtCurEqu,txtCheckNum;
    private TextView txtBalance;
    private Button txtCur,txtTransDate,btnSave,btnChoosePerson,btnCheckDate,btnPaymentMethod,btnChooseBank;
    private RadioButton meRadioBtn,onMeRadioBtn;
    private View btnCalculator,balanceLayout,checkLayout;
    private Transaction mBean;
    private Cur selectedCur;
    private Date selectedTransDate,selectedCheckDate;
    private Person selectedPerson;
    private Bank selectedBank;
    private int selectedType;
    private PaymentMethod selectedPaymentMethod = PaymentMethod.CASH;
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
        txtBank = (AutoCompleteTextView)findViewById(R.id.txtBank);
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
        btnChooseBank = (Button)findViewById(R.id.btnChooseBank);
        btnPaymentMethod = (Button)findViewById(R.id.btnPaymentMethod);
        btnCheckDate = (Button)findViewById(R.id.txtCheckDate);
        txtCheckNum = (EditText)findViewById(R.id.txtCheckNum);
        checkLayout = findViewById(R.id.checkLayout);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnChoosePerson = (Button)findViewById(R.id.btnChoosePerson);

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


        btnChoosePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PersonsActivity.getIntent(AddEditTransactionActivity.this,1, ListActivity.MODE_SEARCH),REQ_SELECT_PERSON);
            }
        });

        btnChooseBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BanksListActivity.getIntent(AddEditTransactionActivity.this,1,ListActivity.MODE_SEARCH),REQ_SELECT_BANK);
            }
        });

        txtCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CurListActivity.getIntent(AddEditTransactionActivity.this,1,ListActivity.MODE_SEARCH),REQ_SELECT_CUR);
            }
        });


        txtTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.newInstance(getCalendar(),REQ_TRANS_DATE).show(getSupportFragmentManager(),"datePicker");
            }
        });

        btnCheckDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.newInstance(getCalendar(),REQ_CHECK_DATE).show(getSupportFragmentManager(),"datePicker");
            }
        });

        btnPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentMethodPopup.IPaymentMethodPopup listener = new PaymentMethodPopup.IPaymentMethodPopup() {
                    @Override
                    public void setPaymentMethod(PaymentMethod paymentMethod) {
                        selectedPaymentMethod = paymentMethod;
                        btnPaymentMethod.setText(paymentMethod.getString(AddEditTransactionActivity.this));
                        if (selectedPaymentMethod == PaymentMethod.CHECK){
                            showCheckLayout();
                            if (selectedCheckDate == null){
                                selectedCheckDate = new Date();
                                btnCheckDate.setText(getString(R.string.checkDate)+"\n"+formatDate(selectedCheckDate));
                            }
                        }else {
                            hideCheckLayout();
                        }
                    }
                };
                new PaymentMethodPopup(AddEditTransactionActivity.this,v,listener).show();
            }
        });

        if (mBean != null){
            mMode = MODE_EDIT;
            selectedPerson = PersonsDB.getInstance(this).getBeanById(mBean.getPersonCode());
            String personName = "";
            if (selectedPerson != null) personName = selectedPerson.getName();
            txtName.setText(personName);
            selectedTransDate = mBean.getTransDate();

            PaymentMethod method = PaymentMethod.get(mBean.getPaymentMethod());
            if (method == null) method = PaymentMethod.CASH;
            selectedPaymentMethod = method;

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
            if (selectedPaymentMethod == PaymentMethod.CHECK){
                showCheckLayout();
                selectedBank = BanksDB.getInstance(this).getBeanById(mBean.getBankCode());
                String bankName = "";
                if (selectedBank != null) bankName = selectedBank.getName();
                txtBank.setText(bankName);
                selectedCheckDate = mBean.getCheckDate();
                btnCheckDate.setText(getString(R.string.checkDate) + "\n" + formatDate(selectedCheckDate));
                txtCheckNum.setText(mBean.getCheckNum());
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
            selectedCheckDate = new Date();
            btnCheckDate.setText(getString(R.string.checkDate) + "\n" + formatDate(selectedCheckDate));
            selectedType = 0;
            meRadioBtn.setChecked(true);
            HandlingMap();
        }
        btnPaymentMethod.setText(selectedPaymentMethod.getString(this));


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

    private void showCheckLayout(){
        checkLayout.setVisibility(View.VISIBLE);
    }

    private void hideCheckLayout(){
        checkLayout.setVisibility(View.GONE);
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
        txtBank.setThreshold(1);
        List<Person> persons = PersonsDB.getInstance(this).getAll(-1,null);
        List<Bank> banks = BanksDB.getInstance(this).getAll();
        String[] arrPersons = new String[persons.size()];
        String[] arrBanks = new String[banks.size()];
        for (int i=0 ; i<persons.size() ; i++){
            arrPersons[i] = persons.get(i).getName();
        }
        for (int i=0 ; i<banks.size() ; i++){
            arrBanks[i] = banks.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrPersons);

        ArrayAdapter<String> adapterBanks = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrBanks);

        txtName.setAdapter(adapter);
        txtBank.setAdapter(adapterBanks);

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
        if (selectedPaymentMethod == PaymentMethod.CHECK){
            if (txtBank.getText().toString().replace(" ","").isEmpty()){
                txtBank.setError(getString(R.string.theBankFieldIsNotValid));
                return getString(R.string.theBankFieldIsNotValid);
            }
            if (txtCheckNum.getText().toString().trim().isEmpty()){
                txtCheckNum.setError(getString(R.string.theCheckNumFieldIsNotValid));
                return getString(R.string.theCheckNumFieldIsNotValid);
            }
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
        if (selectedPaymentMethod == PaymentMethod.CHECK){
            Bank bank = BanksDB.getInstance(this).getBeanByName(txtBank.getText().toString());
            if (bank == null && !txtBank.getText().toString().isEmpty()){
                bank = new Bank();
                String code= UUID.randomUUID().toString();
                bank.setCode(code);
                bank.setName(txtBank.getText().toString());
                BanksDB.getInstance(this).saveBean(bank);
                selectedBank = bank;
            }
            bean.setBankCode(bank.getCode());
            bean.setCheckNum(txtCheckNum.getText().toString());
            bean.setCheckDate(selectedCheckDate);
        }
        bean.setPaymentMethod(selectedPaymentMethod.value());
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

            if (requestCode == REQ_SELECT_BANK){
                selectedBank = data.getParcelableExtra("data");
                if (selectedBank != null)
                    txtBank.setText(selectedBank.getName(),false);
                txtCheckNum.requestFocus();
                txtCheckNum.setFocusable(true);
            }
        }
    }

    private void showCurEquLayout(boolean b) {
        View layout = findViewById(R.id.layoutCurEqu);
        layout.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public void onDateSet(int reqCode,int year, int month, int day) {
        switch (reqCode){
            case REQ_TRANS_DATE:
                selectedTransDate = DateUtils.getDate(year,month,day);
                txtTransDate.setText(getString(R.string.transactionDate) + "\n" + formatDate(selectedTransDate));
                break;
            case REQ_CHECK_DATE:
                selectedCheckDate = DateUtils.getDate(year,month,day);
                btnCheckDate.setText(getString(R.string.checkDate) + "\n" + formatDate(selectedCheckDate));
        }

    }

    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
