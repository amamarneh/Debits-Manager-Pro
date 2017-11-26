package amarnehsoft.com.debits.fragments.listFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.PersonDetailActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.db.PersonsDB;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jcc on 8/18/2017.
 */

public class PersonsListFragment extends ListFragment {

    public static final String ARG_CAT_CODE = "catCode";

    public static Fragment newInstance(int numberOfCols,String catCode,int mode){
        Fragment fragment = new PersonsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        bundle.putString(ARG_CAT_CODE,catCode);
        bundle.putInt(ARG_MODE,mode);
        bundle.putInt(ARG_TYPE_TOOLS,NON_TOOLS);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Adapter mAdapter;

    @Override
    public void setupRecyclerView(){
        List<Person> list = getItems(mQuery);
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

    private List<Person> getItems(String query){
        int isDeleted = 0;
        String catCode = getCatCode();
        if (query == null || query.length() == 0){
            return PersonsDB.getInstance(getContext()).getAll(-1,catCode);
        }
        return PersonsDB.getInstance(getContext()).getSearchQueryResult(query);
    }

    private String getCatCode(){
        return getArguments().getString(ARG_CAT_CODE);
    }

    private class Holder extends ListFragment.Holder<Person>
    {
        private TextView mTextView;

        private Person mItem;
        public Holder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextView=(TextView)itemView.findViewById(R.id.textView);
        }

        public void bind(Person item)
        {
            mItem = item;
            mTextView.setText(item.getName());
        }

        @Override
        public void onClicked(View v)
        {
            if (mMode == MODE_VIEW){
                startActivityForResult(PersonDetailActivity.getIntent(getContext(),mItem.getKey()),REQ_EDIT);
            }else if(mMode == MODE_SEARCH){
                Intent intent = new Intent();
                Bundle bundle  =new Bundle();
                bundle.putParcelable("data",mItem);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK,intent);
                getActivity().finish();
            }
        }
    }

    private class Adapter extends ListFragment.Adapter<Person>
    {

        public Adapter(List<Person> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_person;
        }

        @Override
        public ListFragment.Holder getNewHolder(View v) {
            return new Holder(v);
        }
    }
}
