package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.MainActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.DBHelper;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;

public abstract class ItemDetailActivity<B,T extends DBHelper> extends AppCompatActivity {
    //b : bean type
    //t : database table

    public static final int REQ_EDIT =222;

    protected Class<T> entityClass;
    protected T mTable;
    protected ItemDetailFragment mFragment;
    protected CollapsingToolbarLayout mAppBarLayout;
    protected String mKey;

    protected B mBean;
    public void refresh(B item){
        mBean = item;
        refreshView();
        mFragment.refresh(item);
    }

    protected void setEntityClass(T table){
        this.entityClass = (Class<T>) table.getClass();
        mTable = table;
    }


    protected B setBean(){
//        if(mBean == null){
            mBean = (B)mTable.getBeanById(mKey);
//        }
        return mBean;
    }

    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntityClass(getTable());
        mKey = getIntent().getStringExtra(ARG_ITEM_ID);
        setBean();

        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


//        setBean();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAppBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (mAppBarLayout != null) {
            mAppBarLayout.setTitle(getBarTitle());
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    getIntent().getStringExtra(ARG_ITEM_ID));
            ItemDetailFragment fragment = getFragment();
            mFragment = fragment;

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            navigateUpTo(new Intent(this, MainActivity.class));
            onBackPressed();
            return true;
        }
        if(id == R.id.menu_delete){
            onDeleteMeniItemClicked(mKey);
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteMeniItemClicked(final String key)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.deleteItem))
                .setMessage(getString(R.string.areYouSureYouWantToDeleteThisItem))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onDeleteMenuItemClicked(key);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }
    protected void ShowSnackbar(String msg){
        View layout = findViewById(R.id.coordinatorlayout);
        Snackbar.make(layout,msg,Snackbar.LENGTH_SHORT).show();
    }
    protected abstract void onFabClicked();
    protected abstract ItemDetailFragment getFragment();
    protected abstract String getBarTitle();
    protected abstract void refreshView();
    protected abstract T getTable();
    protected abstract void onDeleteMenuItemClicked(String key);
}
