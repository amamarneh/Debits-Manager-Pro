package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditPeronCatActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonCatsDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.PersonCatDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.PersonDetailFragment;
import amarnehsoft.com.debits.fragments.listFragments.PersonsCatListFragment;
import amarnehsoft.com.debits.fragments.listFragments.PersonsListFragment;
import amarnehsoft.com.debits.utils.Alerts;

/**
 * Created by jcc on 6/3/2017.
 */

public class PersonCatDetailActivity extends ItemDetailActivity<PersonCat,PersonCatsDB> {

    public static Intent getIntent(Context ctx,String catCode){
        Intent intent = new Intent(ctx,PersonCatDetailActivity.class);
        intent.putExtra(ARG_ITEM_ID,catCode);
        return intent;
    }

    @Override
    protected void onFabClicked() {
        startActivityForResult(AddEditPeronCatActivity.getIntent(this,mKey),REQ_EDIT);
    }

    @Override
    protected ItemDetailFragment getFragment()
    {
        ItemDetailFragment fragment = PersonCatDetailFragment.newInstance(mKey);
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
    protected PersonCatsDB getTable() {
        return PersonCatsDB.getInstance(this);
    }

    @Override
    protected void onDeleteMenuItemClicked(String key) {
        if(PersonsDB.getInstance(this).getNoOfPersonsForCat(key) > 0){
            Alerts.MyAlert(this,getString(R.string.cantDelete),getString(R.string.thisCatHasBeenUsedItCantBeDeleted)).show();
        }else {
            if (PersonCatsDB.getInstance(this).deleteBean(key)) {
                setResult(RESULT_OK);
                finish();
            } else {
                super.ShowSnackbar(getString(R.string.cant_delete_this_cat));
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
                setResult(RESULT_OK,data);
            }
        }
    }

}
