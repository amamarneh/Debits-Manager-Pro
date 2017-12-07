package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditCurActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.CurDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.TransactionDetailFragment;
import amarnehsoft.com.debits.utils.Alerts;

/**
 * Created by jcc on 6/3/2017.
 */

public class CurDetailActivity extends ItemDetailActivity<Cur,CurDB> {

    public static Intent getIntent(Context ctx,String curCode){
        Intent intent = new Intent(ctx,CurDetailActivity.class);
        intent.putExtra(ARG_ITEM_ID,curCode);
        return intent;
    }

    @Override
    protected void onFabClicked() {
        startActivityForResult(AddEditCurActivity.getIntent(this,mKey),REQ_EDIT);
    }

    @Override
    protected ItemDetailFragment getFragment()
    {
        ItemDetailFragment fragment = CurDetailFragment.newInstance(mKey);
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
    }

    @Override
    protected CurDB getTable() {
        return CurDB.getInstance(this);
    }

    @Override
    protected void onDeleteMenuItemClicked(String key) {
        if(TransactionsDB.getInstance(this).getNoOfTransactionsForCur(key) > 0){
            Alerts.MyAlert(this,getString(R.string.cantDelete),getString(R.string.thisCurHasBeenUsedItCantBeDeleted)).show();
        }else {
            if (CurDB.getInstance(this).deleteBean(key)) {
                setResult(RESULT_OK);
                finish();
            } else {
                super.ShowSnackbar(getString(R.string.cant_delete_this_cur));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT){
            if (resultCode == RESULT_OK){
                mBean = data.getParcelableExtra("data");
                refreshView();
                mFragment.refresh(mBean);
                setResult(RESULT_OK,data);
            }
        }
    }

}
