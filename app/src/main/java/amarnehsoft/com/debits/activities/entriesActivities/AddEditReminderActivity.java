package amarnehsoft.com.debits.activities.entriesActivities;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.listActivities.CurListActivity;
import amarnehsoft.com.debits.activities.listActivities.ListActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.constants.TransactionType;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.RemindersDB;
import amarnehsoft.com.debits.fragments.dialogs.CreatePersonDialog;
import amarnehsoft.com.debits.fragments.dialogs.DatePickerFragment;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.NotificationUtils;
import amarnehsoft.com.debits.utils.NumberUtils;
import amarnehsoft.com.debits.utils.ReminderContentBuilder;

public class AddEditReminderActivity extends AddEditActivity implements DatePickerFragment.IDatePickerFragment{

    public static final int REQ_SELECT_PERSON = 1;
    public static final int REQ_SELECT_CUR=2;

    public static final int REQ_TRANS_DATE = 3;
    public static final int REQ_REMINDER_DATE= 4;

    private AutoCompleteTextView txtName;
    private EditText txtAmount, txtNotes;
    private Button txtCur,txtTransDate,btnReminderDate;
    private RadioButton meRadioBtn,onMeRadioBtn;
    private Button btnSave;
    private Button btnChoosePerson;

    private Reminder mBean;
    private Cur selectedCur;
    private Date selectedTransDate;
    private Date selectedReminderDate;

    private Person selectedPerson;
    private int selectedType;
//    private int mode;

    public static Intent getIntent(Context context,String reminderCode){
        Intent intent = new Intent(context,AddEditReminderActivity.class);
        intent.putExtra(ARG_CODE,reminderCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_reminder);

        String code =  getIntent().getStringExtra(ARG_CODE);
        if (code != null){
            mBean = RemindersDB.getInstance(this).getBeanById(code);
        }
        selectedType = TransactionType.PAYMENT.value();

        txtName = (AutoCompleteTextView) findViewById(R.id.txtName);
        setupAutoComplete();
        txtAmount = (EditText)findViewById(R.id.txtAmount);
        txtCur = (Button)findViewById(R.id.txtCur);
        txtNotes = (EditText)findViewById(R.id.txtNotes);
        txtTransDate = (Button)findViewById(R.id.txtTransDate);
        meRadioBtn = (RadioButton)findViewById(R.id.meRadioBtn);
        onMeRadioBtn = (RadioButton)findViewById(R.id.onMeRadioBtn);
        btnReminderDate = (Button)findViewById(R.id.btnReminderDate);

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
                startActivityForResult(PersonsActivity.getIntent(AddEditReminderActivity.this,1, ListActivity.MODE_SEARCH),REQ_SELECT_PERSON);
            }
        });

        txtCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CurListActivity.getIntent(AddEditReminderActivity.this,1,ListActivity.MODE_SEARCH),REQ_SELECT_CUR);
            }
        });
        btnSave = (Button)findViewById(R.id.btnSave);

        txtTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.newInstance(getTransCalendar(),REQ_TRANS_DATE).show(getSupportFragmentManager(),"datePicker");
            }
        });

        btnReminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment.newInstance(getReminderCalendar(),REQ_REMINDER_DATE).show(getSupportFragmentManager(),"datePicker");
            }
        });

        if (mBean != null){
            mMode = MODE_EDIT;
            selectedPerson = PersonsDB.getInstance(this).getBeanById(mBean.getPersonCode());
            String name="";
            if (selectedPerson != null) name = selectedPerson.getName();
            txtName.setText(name);
            selectedTransDate = mBean.getTransDate();
            selectedReminderDate = mBean.getReminerDate();
            txtTransDate.setText(getString(R.string.transactionDate) + "\n" + formatDate(selectedTransDate));
            btnReminderDate.setText(getString(R.string.reminderDate) + "\n" + formatDate(selectedReminderDate));
            txtNotes.setText(mBean.getNotes());
            selectedCur = CurDB.getInstance(this).getBeanById(mBean.getCurCode());
            String curName = getString(R.string.not_found);
            if (selectedCur != null) curName = selectedCur.getName();
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
            selectedCur = CurDB.getInstance(this).getDefualtBean();
            String curName = getString(R.string.not_found);
            if (selectedCur != null) curName = selectedCur.getName();
            txtCur.setText(curName);
            selectedTransDate = new Date();
            selectedReminderDate = new Date();
            txtTransDate.setText(getString(R.string.transactionDate) + "\n" +  formatDate(selectedTransDate));
            btnReminderDate.setText(getString(R.string.reminderDate)  + "\n" + formatDate(selectedReminderDate));
            meRadioBtn.setChecked(true);
        }


        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCommand();
            }
        });
        //
        setupTitle(selectedType);
        txtAmount.requestFocus();
    }

    private Calendar getTransCalendar(){
        return DateUtils.getCalendarFromDate(selectedTransDate);
    }
    private Calendar getReminderCalendar(){
        return DateUtils.getCalendarFromDate(selectedReminderDate);
    }

    private void setupTitle(int selectedType) {

        if(mMode == MODE_ADD) {
//            if (selectedType == 0)
//                setTitle(getString(R.string.add_debit));
//            else if (selectedType == 1)
//                setTitle(getString(R.string.add_payment));
            setTitle(R.string.addReminder);
        }else
            if(mMode == MODE_EDIT){
//                if(selectedType == 0)
//                    setTitle(getString(R.string.update_debit));
//                else
//                if(selectedType == 1)
//                    setTitle(getString(R.string.update_payment));
                setTitle(getString(R.string.updateRemider));
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
    }

    private String formatDate(Date date){
        return DateUtils.formatDate(date);
    }

    private void saveCommand(){
        String validation = validate();
        if (validation == null){
            trySave(txtName.getText().toString());
        }else {
            showError(validation);
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

        if (DateUtils.compare(selectedReminderDate,new Date()) != 1){
//            txtName.setError(getString(R.string.theReminderDateWasGone));
            return getString(R.string.theReminderDateWasGone);
        }
        return null;
    }

    private void save(Person person){
        Reminder bean = new Reminder();
        if (mMode == MODE_ADD){
            bean.setCode(UUID.randomUUID().toString());
        }else if (mMode == MODE_EDIT){
            bean.setCode(mBean.getCode());
        }

        String personCode = null;
        if (person != null) personCode = person.getKey();
        bean.setPersonCode(personCode);
        bean.setNotes(txtNotes.getText().toString());
        String curCode = null;
        if (selectedCur != null) curCode = selectedCur.getCode();
        bean.setCurCode(curCode);
        bean.setAmount(NumberUtils.getDouble(txtAmount.getText().toString()));
        bean.setCreationDate(new Date());
        bean.setTransDate(selectedTransDate);
        bean.setType(selectedType);
        bean.setReminerDate(selectedReminderDate);

        RemindersDB.getInstance(this).saveBean(bean);
        Notification notification = NotificationUtils.getNotification(this, ReminderContentBuilder.buildTitle(this,bean),ReminderContentBuilder.buildContent(this,bean));
        long ms = DateUtils.getDiffInMilliSeconds(bean.getReminerDate(),new Date());
        NotificationUtils.scheduleNotification(this,notification,ms,1);

        Intent intent = new Intent();
        intent.putExtra("data",bean);
        Log.e("Amarneh","bean.getAmount="+bean.getAmount());
        //Bundle bundle = new Bundle();
        //bundle.putParcelable("data",bean);
        //intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

        finish();
    }

    private void showError(String error){
        ShowSnackbar(error);
    }

    private void trySave(String personName){
        final Person person = PersonsDB.getInstance(this).getBeanByName(personName);
        if (person == null){
            new CreatePersonDialog(this, personName) {
                @Override
                public void savedSuccessfully(Person person2) {
                    save(person2);
                }
            }.show();
        }else {
            save(person);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                if (selectedCur != null)
                    txtCur.setText(selectedCur.getName());
            }
        }
    }

    @Override
    public void onDateSet(int reqCode,int year, int month, int day) {
        if (reqCode == REQ_TRANS_DATE){
            selectedTransDate = DateUtils.getDate(year,month,day);
            txtTransDate.setText(getString(R.string.transactionDate) + "\n" + formatDate(selectedTransDate));
        }else if (reqCode == REQ_REMINDER_DATE){
            selectedReminderDate = DateUtils.getDate(year,month,day);
            btnReminderDate.setText(getString(R.string.reminderDate) + "\n" + formatDate(selectedReminderDate));
        }
    }
    @Override
    protected void OnSaveClick() {
        saveCommand();
    }
}
