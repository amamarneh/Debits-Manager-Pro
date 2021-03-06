package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditTransactionActivity;
import amarnehsoft.com.debits.beans.CustomTransaction;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.fragments.dialogs.GenerateExcelDialogFragment;
import amarnehsoft.com.debits.fragments.listFragments.TransactionListFragment;
import amarnehsoft.com.debits.fragments.tabedFragments.TransactionsTabedFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class TransactionListActivity extends ListActivity {

    public static final String ARG_TYPE="type";
    public static final String ARG_PERSON_CODE="personCode";

    public static Intent getIntent(Context ctx,int numberOfCols,int type,String personCode){
        Intent intent = new Intent(ctx,TransactionListActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCols);
        intent.putExtra(ARG_TYPE,type);
        intent.putExtra(ARG_PERSON_CODE,personCode);
        return intent;
    }

    @Override
    public void onFabClicked() {
        HashMap<String,Object> map = new HashMap<>();
        Person person = PersonsDB.getInstance(this).getBeanById(getPersonCode());
        map.put("person",person);
        map.put("type",getType());
        startActivityForResult(AddEditTransactionActivity.getIntent(this,null,map),REQ_ADD);
    }


    @Override
    public Fragment getFragment() {
        if(getType() == 1)
        setTitle(getString(R.string.payments));
        else if(getType() == 0)
            setTitle(getString(R.string.debits));
        else
            setTitle(getString(R.string.debits_and_payments));
        return TransactionListFragment.newInstance(1,getType(),getPersonCode());
    }

    private int getType(){
        return getIntent().getIntExtra(ARG_TYPE,-1);
    }

    private String getPersonCode(){
        return getIntent().getStringExtra(ARG_PERSON_CODE);
    }

    @Override
    public void onQueryTextChanged(String query) {
        mFragment.onQueryTextChanged(query);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD){
            if (resultCode== RESULT_OK){
                mFragment.setupRecyclerView();
                ShowSnackbar(getString(R.string.newTransactionAddedSuccessfully));
            }
        }
    }

    @Override
    protected boolean showExcelIcon() {
        return true;
    }

    @Override
    protected void onExcelIconClicked() {
        super.onExcelIconClicked();
        List<CustomTransaction> list = mFragment.getAdapter().getList();
        List<Transaction>  result = new ArrayList<>();
        for (Transaction t : list){
            Transaction n = TransactionsDB.getInstance(this).getBeanById(t.getCode());
            if (n != null)
                result.add(n);
        }
        GenerateExcelDialogFragment.newInstance((ArrayList<Transaction>) result,getString(R.string.transactions))
                .show(getSupportFragmentManager(),GenerateExcelDialogFragment.TAG);
    }
}
