package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditTransactionActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.PersonCatDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.TransactionDetailFragment;

/**
 * Created by jcc on 6/3/2017.
 */

public class TransactionDetailActivity extends ItemDetailActivity<Transaction,TransactionsDB> {

    public static Intent getIntent(Context ctx,String transactionCode){
        Intent intent = new Intent(ctx,TransactionDetailActivity.class);
        intent.putExtra(ARG_ITEM_ID,transactionCode);
        return intent;
    }

    @Override
    protected void onFabClicked() {
        startActivityForResult(AddEditTransactionActivity.getIntent(this,mKey,null),REQ_EDIT);
    }

    @Override
    protected ItemDetailFragment getFragment()
    {
        ItemDetailFragment fragment = TransactionDetailFragment.newInstance(mKey);
        return fragment;
    }

    @Override
    protected String getBarTitle()
    {
        if(mBean == null)
            return "non";
        if(mBean.getType() == 0)
            return getString(R.string.debit);
        return getString(R.string.payment);
//        Person person = PersonsDB.getInstance(this).getBeanById(getBean().getPersonCode());
//        String title=  person.getName() ;
//       return title;
    }

    @Override
    protected void refreshView() {
        mAppBarLayout.setTitle(getBarTitle());
    }

    @Override
    protected TransactionsDB getTable() {
        return TransactionsDB.getInstance(this);
    }

    @Override
    protected void onDeleteMenuItemClicked(String key) {
        if (TransactionsDB.getInstance(this).deleteBean(key)){
            setResult(RESULT_OK);
            finish();
        }else{
            super.ShowSnackbar(getString(R.string.cant_delete_this_transaction));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT){
            if (resultCode == RESULT_OK){
                Transaction result = data.getParcelableExtra("data");
                mBean = TransactionsDB.getInstance(this).getBeanById(result.getCode());
                Log.d("Amarneh","bean.getCode="+mBean.getCode());
                refreshView();
                mFragment.refresh(mBean);
                setResult(RESULT_OK,data);
            }
        }
    }
}
