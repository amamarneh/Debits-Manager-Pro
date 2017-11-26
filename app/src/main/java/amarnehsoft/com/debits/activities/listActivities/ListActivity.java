package amarnehsoft.com.debits.activities.listActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.fragments.listFragments.ListFragment;
import amarnehsoft.com.debits.interfaces.SearchViewExpandListener;

public abstract class ListActivity extends AppCompatActivity {

    public static final String ARG_NUMBER_OF_COLS = "numberOfCols";
    public static final String ARG_MODE = "mode";

    public static final String ARG_FAB_MODE = "fabMode";
    public static final int MODE_PERSONS = 1;
    public static final int MODE_OTHER = 0;

    public static final int MODE_VIEW = 0;
    public static final int MODE_SEARCH = 1;

    public static final int REQ_ADD =111;

    protected int mMode;

    ListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked();
            }
        });
        if(getIntent().getIntExtra(ARG_FAB_MODE,MODE_OTHER) == MODE_PERSONS)
            fab.setImageResource(R.drawable.ic_person_add_white_24dp);

        mMode = getIntent().getIntExtra(ARG_MODE,MODE_VIEW);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (ListFragment) fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment =(ListFragment) getFragment();
            fm.beginTransaction().replace(R.id.fragmentContainer,mFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // See above
        MenuItemCompat.setOnActionExpandListener(searchItem, new SearchViewExpandListener(this));
        MenuItemCompat.setActionView(searchItem, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryTextSubmited(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onQueryTextChanged(s);
                return false;
            }
        });

        return true;
    }

    protected int getNumberOfCols(){
        int numberOfCols = getIntent().getIntExtra(ARG_NUMBER_OF_COLS,1);
        if (numberOfCols > 0){
            return numberOfCols;
        }else {
            return 1;
        }
    }
    protected void ShowSnackbar(String msg){
        View layout = findViewById(R.id.coordinatorlayout);
        Snackbar.make(layout,msg,Snackbar.LENGTH_SHORT).show();
    }

    protected void onQueryTextSubmited(String query){
        //you should override it if you want to use it .
    }

    public abstract void onFabClicked();
    public abstract Fragment getFragment();
    public abstract void onQueryTextChanged(String query);
}
