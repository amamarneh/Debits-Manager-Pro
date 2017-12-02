package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditBankActivity;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditPeronCatActivity;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.fragments.listFragments.BanksListFragment;
import amarnehsoft.com.debits.fragments.listFragments.PersonsCatListFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class BanksListActivity extends ListActivity {

    public static Intent getIntent(Context ctx,int numberOfCols,int mode){
        Intent intent = new Intent(ctx,BanksListActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCols);
        intent.putExtra(ARG_MODE,mode);
        return intent;
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(AddEditBankActivity.getIntent(this,null),REQ_ADD);
    }

    @Override
    public Fragment getFragment() {
        return BanksListFragment.newInstance(getNumberOfCols(),mMode);
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
                Bank bank = data.getParcelableExtra("data");
                mFragment.setupRecyclerView();
                if (bank != null)
                 super.ShowSnackbar(getString(R.string.newBankHasBeenAddedSuccessfully) + " ( " + bank.getName() + " )");
            }
        }
    }
}
