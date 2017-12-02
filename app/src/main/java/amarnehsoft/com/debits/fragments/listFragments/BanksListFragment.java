package amarnehsoft.com.debits.fragments.listFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.PersonCatDetailActivity;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.BanksDB;
import amarnehsoft.com.debits.db.PersonCatsDB;

/**
 * Created by jcc on 8/18/2017.
 */

public class BanksListFragment extends ListFragment {

    public static Fragment newInstance(int numberOfCols,int mode){
        Fragment fragment = new BanksListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        bundle.putInt(ARG_MODE,mode);
        bundle.putInt(ARG_TYPE_TOOLS,NON_TOOLS);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Adapter mAdapter;

    @Override
    public void setupRecyclerView(){
        List<Bank> list = getItems(mQuery);
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

    private List<Bank> getItems(String query){
        if (query == null || query.length() == 0)
            return BanksDB.getInstance(getContext()).getAll();
        else
            return BanksDB.getInstance(getContext()).getSearchQueryResult(query);
    }

    private class Holder extends ListFragment.Holder<Bank>
    {
        private TextView mTextView;

        private Bank mItem;
        public Holder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextView=(TextView)itemView.findViewById(R.id.textView);
        }

        public void bind(Bank item)
        {
            mItem = item;
            mTextView.setText(item.getName());
        }

        @Override
        public void onClicked(View v)
        {
            if (mMode == MODE_VIEW){
                //startActivityForResult(PersonCatDetailActivity.getIntent(getContext(),mItem.getCode()),REQ_EDIT);
            } else if(mMode == MODE_SEARCH){
                Intent intent = new Intent();
                Bundle bundle  =new Bundle();
                bundle.putParcelable("data",mItem);
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        }
    }

    private class Adapter extends ListFragment.Adapter<Bank>
    {
        public Adapter(List<Bank> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.simple_row;
        }

        @Override
        public ListFragment.Holder getNewHolder(View v) {
            return new Holder(v);
        }
    }
}
