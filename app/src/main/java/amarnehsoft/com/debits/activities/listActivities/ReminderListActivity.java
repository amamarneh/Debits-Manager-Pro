package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditReminderActivity;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditTransactionActivity;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.fragments.listFragments.ReminderListFragment;
import amarnehsoft.com.debits.fragments.tabedFragments.ReminderTabedFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class ReminderListActivity extends ListActivity {

    public static final String ARG_TYPE="type";

    public static Intent getIntent(Context ctx,int numberOfCols,int type){
        Intent intent = new Intent(ctx,ReminderListActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCols);
        intent.putExtra(ARG_TYPE,type);
        return intent;
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(AddEditReminderActivity.getIntent(this,null),REQ_ADD);
    }

    @Override
    public Fragment getFragment() {
        return ReminderListFragment.newInstance(getNumberOfCols(),getType(),null);
    }

    private int getType(){
        return getIntent().getIntExtra(ARG_TYPE,-1);
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
                Reminder bean = data.getParcelableExtra("data");
                mFragment.setupRecyclerView();
                super.ShowSnackbar("[" + " reminder " + "] " +  getString(R.string.added_successfully));
            }
        }
    }
}
