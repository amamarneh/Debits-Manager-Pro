package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditPeronCatActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.fragments.listFragments.PersonsCatListFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonsCatListActivity extends ListActivity {

    public static Intent getIntent(Context ctx,int numberOfCols,int mode){
        Intent intent = new Intent(ctx,PersonsCatListActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCols);
        intent.putExtra(ARG_MODE,mode);
        return intent;
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(AddEditPeronCatActivity.getIntent(this,null),REQ_ADD);
    }

    @Override
    public Fragment getFragment() {
        return PersonsCatListFragment.newInstance(getNumberOfCols(),mMode);
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
                PersonCat cat = data.getParcelableExtra("data");
                mFragment.setupRecyclerView();
                if (cat != null)
                 super.ShowSnackbar("[" + cat.getName() + "] " +  getString(R.string.added_successfully));
            }
        }
    }
}
