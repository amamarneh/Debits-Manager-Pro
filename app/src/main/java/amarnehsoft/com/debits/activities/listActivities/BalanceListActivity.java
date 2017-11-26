package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import amarnehsoft.com.debits.activities.entriesActivities.AddEditTransactionActivity;
import amarnehsoft.com.debits.fragments.listFragments.BalanceListFragment;
import amarnehsoft.com.debits.fragments.tabedFragments.BalanceTabedFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class BalanceListActivity extends ListActivity {

    public static final String ARG_SELECTED_TAB="selectedTab";

    public static Intent getIntent(Context ctx,int numberOfCols,int selectedTab){
        Intent intent = new Intent(ctx,BalanceListActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCols);
        intent.putExtra(ARG_SELECTED_TAB,selectedTab);
        return intent;
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(AddEditTransactionActivity.getIntent(this,null,null),REQ_ADD);
    }

    @Override
    public Fragment getFragment() {
        return BalanceListFragment.newInstance(1,-1);
    }

    private int getSelectedTab(){
        return getIntent().getIntExtra(ARG_SELECTED_TAB,0);
    }

    @Override
    public void onQueryTextChanged(String query) {
        mFragment.onQueryTextChanged(query);
    }
}
