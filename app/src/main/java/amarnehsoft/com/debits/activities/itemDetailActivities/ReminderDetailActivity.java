package amarnehsoft.com.debits.activities.itemDetailActivities;

import android.content.Context;
import android.content.Intent;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.entriesActivities.AddEditReminderActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.RemindersDB;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ItemDetailFragment;
import amarnehsoft.com.debits.fragments.itemDetailsFragment.ReminderDetailFragment;

/**
 * Created by jcc on 6/3/2017.
 */

public class ReminderDetailActivity extends ItemDetailActivity<Reminder,RemindersDB> {

    public static Intent getIntent(Context ctx,String reminderCode){
        Intent intent = new Intent(ctx,ReminderDetailActivity.class);
        intent.putExtra(ARG_ITEM_ID,reminderCode);
        return intent;
    }

    @Override
    protected void onFabClicked() {
        startActivityForResult(AddEditReminderActivity.getIntent(this,mKey),REQ_EDIT);
    }

    @Override
    protected ItemDetailFragment getFragment()
    {
        ItemDetailFragment fragment = ReminderDetailFragment.newInstance(mKey);
        return fragment;
    }

    @Override
    protected String getBarTitle()
    {
        Person person = PersonsDB.getInstance(this).getBeanById(mBean.getPersonCode());
        String personName = getString(R.string.not_found);
        if (person != null) personName = person.getName();
        String title= getString(R.string.reminder);
       return title;
    }

    @Override
    protected void refreshView() {
        Person person = PersonsDB.getInstance(this).getBeanById(mBean.getPersonCode());
        String personName = getString(R.string.not_found);
        if (person != null) personName = person.getName();
        String title= getString(R.string.reminderFor) + " (" + personName + ")";
        mAppBarLayout.setTitle(title);
    }

    @Override
    protected RemindersDB getTable() {
        return RemindersDB.getInstance(this);
    }

    @Override
    protected void onDeleteMenuItemClicked(String key) {
        if (RemindersDB.getInstance(this).deleteBean(key)){
            setResult(RESULT_OK);
            finish();
        }else{
            super.ShowSnackbar(getString(R.string.cant_delete_this_reminder));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT){
            if (resultCode == RESULT_OK){
                Reminder reminder = data.getParcelableExtra("data");
                mBean = RemindersDB.getInstance(this).getBeanById(reminder.getCode());
                refreshView();
                setResult(RESULT_OK,data);
                mFragment.refresh(mBean);
            }
        }
    }
}
