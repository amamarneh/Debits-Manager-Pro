package amarnehsoft.com.debits.interfaces;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by jcc on 8/18/2017.
 */

public class SearchViewExpandListener implements MenuItemCompat.OnActionExpandListener {

    private Context context;

    public SearchViewExpandListener (Context c) {
        context = c;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(false);
        return false;
    }
}
