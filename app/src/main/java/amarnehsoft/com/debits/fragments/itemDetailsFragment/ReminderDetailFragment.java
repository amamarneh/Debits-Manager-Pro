package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ItemDetailActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.RemindersDB;
import amarnehsoft.com.debits.utils.DateUtils;

public class ReminderDetailFragment extends ItemDetailFragment<Reminder> {

    private TextView txtPerson,txtReminderDate,txtAmount,txtCur, txtNotes;

    public static ItemDetailFragment newInstance(String reminderCode){
        ItemDetailFragment fragment = new ReminderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailActivity.ARG_ITEM_ID,reminderCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ReminderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBean();
    }

    private void setBean(){
        if (getArguments().containsKey(ItemDetailActivity.ARG_ITEM_ID)) {
            mItem = RemindersDB.getInstance(getContext()).getBeanById(getArguments().getString(ItemDetailActivity.ARG_ITEM_ID));
        }else {
            mItem=null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder_details, container, false);
        txtPerson = (TextView)rootView.findViewById(R.id.txtPersonName);
        txtAmount = (TextView)rootView.findViewById(R.id.txtAmount);
        txtCur = (TextView)rootView.findViewById(R.id.txtCur);
        txtNotes= (TextView)rootView.findViewById(R.id.txtNotes);
        txtReminderDate=  (TextView)rootView.findViewById(R.id.txtReminderDate);

        if (mItem != null) {
            refreshView();
        }

        return rootView;
    }

    @Override
    protected void refreshView() {
        Person person = PersonsDB.getInstance(getContext()).getBeanById(mItem.getPersonCode());
        String personName = getString(R.string.not_found);
        if (person != null) personName = person.getName();
        txtPerson.setText(personName);
        txtReminderDate.setText(DateUtils.formatDate(mItem.getReminerDate()));
        txtAmount.setText(mItem.getAmount()+"");

        Cur cur = CurDB.getInstance(getContext()).getBeanById(mItem.getCurCode());
        String curName = getString(R.string.not_found);
        if (cur != null) curName = cur.getName();
        txtCur.setText(curName);

        txtNotes.setText(mItem.getNotes());
    }
}
