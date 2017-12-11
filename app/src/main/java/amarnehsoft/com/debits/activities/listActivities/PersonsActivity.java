package amarnehsoft.com.debits.activities.listActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditPeronActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.fragments.dialogs.GenerateExcelDialogFragment;
import amarnehsoft.com.debits.fragments.listFragments.PersonsListFragment;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonsActivity extends ListActivity {

    public static Intent getIntent(Context ctx,int numberOfCos,int mode){
        Intent intent = new Intent(ctx,PersonsActivity.class);
        intent.putExtra(ARG_NUMBER_OF_COLS,numberOfCos);
        intent.putExtra(ARG_MODE,mode);
        intent.putExtra(ARG_FAB_MODE,MODE_PERSONS);
        return intent;
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(AddEditPeronActivity.getIntent(this,null),REQ_ADD);
    }

    @Override
    public Fragment getFragment() {
        return PersonsListFragment.newInstance(getNumberOfCols(),null,mMode);
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
                Person person = data.getParcelableExtra("data");
                mFragment.setupRecyclerView();
                if (person != null)
                super.ShowSnackbar(getString(R.string.newPersonHasBeenAddedSuccessfully) + " ( " + person.getName() + " )");
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
        GenerateExcelDialogFragment.newInstance((ArrayList<Parcelable>) mFragment.getAdapter().getList(),getString(R.string.persons))
        .show(getSupportFragmentManager(),GenerateExcelDialogFragment.TAG);
    }
}
