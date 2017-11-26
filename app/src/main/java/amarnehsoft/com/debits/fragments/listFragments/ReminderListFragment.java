package amarnehsoft.com.debits.fragments.listFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ReminderDetailActivity;
import amarnehsoft.com.debits.activities.itemDetailActivities.TransactionDetailActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Reminder;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.RemindersDB;
import amarnehsoft.com.debits.db.TransactionsDB;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.MyColors;

/**
 * Created by jcc on 8/18/2017.
 */

public class ReminderListFragment extends ListFragment {

    public static final String ARG_PERSON_CODE="personCode";

    public static Fragment newInstance(int numberOfCols,int type,String personCode){
        Fragment fragment = new ReminderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        bundle.putInt(ARG_TYPE,type);
        bundle.putString(ARG_PERSON_CODE,personCode);
        bundle.putInt(ARG_TYPE_TOOLS,NON_TOOLS);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Adapter mAdapter;

    @Override
    public void setupRecyclerView(){
        List<Reminder> list = getItems(mQuery);
        if (mAdapter == null)
        {
            mAdapter = new Adapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onQueryTextChanged(String query) {
        mQuery = query;
        setupRecyclerView();
    }

    private String getPersonCode(){
        return getArguments().getString(ARG_PERSON_CODE);
    }

    private List<Reminder> getItems(String query){
        String personCode = getPersonCode();
        int type = getType();
        if (personCode != null){
            //show for the person
            return RemindersDB.getInstance(getContext()).getRemindersOfPerson(personCode,type);
        }else {
            //show for all persons
            if (query == null || query.length() == 0)
                return RemindersDB.getInstance(getContext()).getAll(getType());
            else
                return RemindersDB.getInstance(getContext()).getSearchQueryResult(getType(),query);
        }
    }

    private int getType(){
        return getArguments().getInt(ARG_TYPE,-1);
    }

    private class Holder extends ListFragment.Holder<Reminder>
    {
        private Reminder mItem;
        private TextView dateTextView,priceTextView,personTextView,curTextView;
        public Holder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            dateTextView =(TextView)itemView.findViewById(R.id.dateTextView);
            priceTextView =(TextView)itemView.findViewById(R.id.priceTextView);
            personTextView =(TextView)itemView.findViewById(R.id.personTextView);
            curTextView = (TextView)itemView.findViewById(R.id.cur_text_view);
        }

        public void bind(Reminder item)
        {
            mItem = item;
            dateTextView.setText(DateUtils.formatDate(mItem.getReminerDate()));
            priceTextView.setText(mItem.getAmount() + "");
            Person person =  PersonsDB.getInstance(getContext()).getBeanById(mItem.getPersonCode());
            String personName = getString(R.string.not_found);
            if (person != null) personName = person.getName();
            personTextView.setText(personName);
            if(mItem.getType() == 0)
                priceTextView.setTextColor(MyColors.debitColor);
            else
                priceTextView.setTextColor(MyColors.paymentColor);

            Cur cur = CurDB.getInstance(getContext()).getBeanById(mItem.getCurCode());
            String curName = getString(R.string.not_found);
            if (cur != null) curName = cur.getName();
            curTextView.setText(curName);
        }

        @Override
        public void onClicked(View v)
        {
            startActivityForResult(ReminderDetailActivity.getIntent(getContext(),mItem.getCode()),REQ_EDIT);
        }
    }

    private class Adapter extends ListFragment.Adapter<Reminder>
    {

        public Adapter(List<Reminder> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_debit;
        }

        @Override
        public ListFragment.Holder getNewHolder(View v) {
            return new Holder(v);
        }
    }
}
