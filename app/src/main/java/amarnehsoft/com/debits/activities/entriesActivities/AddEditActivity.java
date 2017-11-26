package amarnehsoft.com.debits.activities.entriesActivities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import amarnehsoft.com.debits.R;

/**
 * Created by jcc on 8/22/2017.
 */

public abstract class AddEditActivity extends AppCompatActivity {
    public static final String ARG_CODE = "code";
    public static final int MODE_ADD=0;
    public static final int MODE_EDIT=1;

    protected int mMode;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_item_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_menu) {
            OnSaveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void ShowSnackbar(String msg){
        View layout = findViewById(R.id.coordinatorlayout);
        Snackbar.make(layout,msg,Snackbar.LENGTH_SHORT).show();
    }
    protected void OnSaveClick() {
    }
}
