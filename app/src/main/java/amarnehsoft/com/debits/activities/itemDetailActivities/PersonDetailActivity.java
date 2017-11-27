package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditPeronActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.TransactionsDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.PersonDetailFragment;
import amarnehsoft.com.debits.utils.Alerts;

/**
 * Created by jcc on 6/3/2017.
 */

public class PersonDetailActivity extends ItemDetailActivity<Person,PersonsDB> {
    private ItemDetailFragment mFragment;
    public static Intent getIntent(Context ctx,String personCode){
        Intent intent = new Intent(ctx,PersonDetailActivity.class);
        intent.putExtra(ARG_ITEM_ID,personCode);
        return intent;
    }

    @Override
    protected void onFabClicked() {
        startActivityForResult(AddEditPeronActivity.getIntent(this,mKey),REQ_EDIT);
    }

    @Override
    protected ItemDetailFragment getFragment()
    {
        ItemDetailFragment fragment = PersonDetailFragment.newInstance(mKey);
        mFragment = fragment;
        return fragment;
    }

    @Override
    protected String getBarTitle()
    {
        String name = getString(R.string.not_found);
        if (mBean != null)
            name = mBean.getName();
        return name;
    }

    @Override
    protected void refreshView() {
        String name = getString(R.string.not_found);
        if (mBean != null)
            name = mBean.getName();
        mAppBarLayout.setTitle(name);
        if(mFragment != null && mFragment instanceof PersonDetailFragment)
            ((PersonDetailFragment) mFragment).refreshView();
    }

    @Override
    protected PersonsDB getTable() {
        return PersonsDB.getInstance(this);
    }

    @Override
    protected void onDeleteMenuItemClicked(String key) {
        int count = TransactionsDB.getInstance(this).getNoOfTransactionsForPerson(key);
            if(count > 0){
                Alerts.MyAlert(this,getString(R.string.cantDelete),getString(R.string.thisPersonHasTransactionsSoItCantBeDeleted)).show();
            }else {
                if (PersonsDB.getInstance(this).deleteBean(key)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ShowSnackbar(getString(R.string.cant_delete_this_person));
                }
            }
    }
    private void onClearClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.clearTransactions))
                .setMessage(getString(R.string.areYouSureYouWantToDeleteAllTransactionsForThisPerson))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TransactionsDB.getInstance(PersonDetailActivity.this).DeleteAllForPerson(mBean.getKey());
                        refreshView();
                        Toast.makeText(PersonDetailActivity.this,getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_clear) {
            onClearClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_detail_person, menu);
        return true;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.item_detail_person, menu);
//        return true;
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT){
            if (resultCode == RESULT_OK){
                mBean = data.getParcelableExtra("data");
                refreshView();
                setResult(RESULT_OK,data);
            }
        }
    }
}
