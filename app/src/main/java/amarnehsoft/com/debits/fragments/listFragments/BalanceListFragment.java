package amarnehsoft.com.debits.fragments.listFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.PersonDetailActivity;
import amarnehsoft.com.debits.beans.Balance;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.CustomBalance;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.db.BalancesDB;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.fragments.dialogs.DatePickerFragment;
import amarnehsoft.com.debits.utils.MyColors;
import amarnehsoft.com.debits.utils.NumberUtils;

/**
 * Created by jcc on 8/18/2017.
 */

public class BalanceListFragment extends ListFragment {
    private int OrderType = 0; // 0 -> By name, 1 -> By Price
    private Boolean Ordered = true; // true -> ASC, False-> DESC
    public static Fragment newInstance(int numberOfCols,int type){
        Fragment fragment = new BalanceListFragment();
        Log.e("Amarneh","its a new fragment");

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        bundle.putInt(ARG_TYPE,type);
        bundle.putInt(ARG_TYPE_TOOLS,BASIC_TOOLS);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Adapter mAdapter;

    @Override
    public void setupRecyclerView(){
        List<CustomBalance> list = getItems(OrderType,mQuery,Ordered);
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
    protected void OBPriceClick() {
        OrderType = 1;
        Ordered = !Ordered;
        UpdateListAndRecycler();
    }

    @Override
    protected void OBNameClick() {
        OrderType = 0;
        Ordered = !Ordered;
        UpdateListAndRecycler();
    }

    private void UpdateListAndRecycler() {
        setupRecyclerView();
    }

    @Override
    public void onQueryTextChanged(String query) {
        mQuery = query;
        UpdateListAndRecycler();
    }

    private List<CustomBalance> getItems(int Ordertype, String query,Boolean order){
//        return BalancesController.getBalances(getContext(),type,query);
        BalancesDB db = new BalancesDB(getContext());
        return db.getCustomBalances(query,Ordertype,order);
    }

    private int getType(){
        return getArguments().getInt(ARG_TYPE,-1);
    }

    private class Holder extends ListFragment.Holder<CustomBalance>
    {
        private CustomBalance mItem;
        private TextView personTextView,priceTextView,curTextView;

        public Holder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            personTextView=(TextView)itemView.findViewById(R.id.personTextView);
            priceTextView=(TextView)itemView.findViewById(R.id.priceTextView);
            curTextView = (TextView)itemView.findViewById(R.id.cur_text_view);
        }

        public void bind(CustomBalance item) {
            mItem = item;
            Cur defaultCur = CurDB.getInstance(getContext()).getDefualtBean();
            double balance = NumberUtils.Round(mItem.getAmount()) ;
            priceTextView.setText(Math.abs(balance)+" " + defaultCur.getName());
            personTextView.setText(mItem.getName());

            curTextView.setText("");

            if(balance >  0)
                priceTextView.setTextColor(MyColors.debitColor);
            else
                priceTextView.setTextColor(MyColors.paymentColor);

        }

        @Override
        public void onClicked(View v) {
            startActivity(PersonDetailActivity.getIntent(getContext(),mItem.getPersonCode()));
        }
    }

    private class Adapter extends ListFragment.Adapter<CustomBalance>
    {

        public Adapter(List<CustomBalance> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_balance;
        }

        @Override
        public ListFragment.Holder getNewHolder(View v) {
            return new Holder(v);
        }
    }
}
