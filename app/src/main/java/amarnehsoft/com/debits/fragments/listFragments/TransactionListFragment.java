package amarnehsoft.com.debits.fragments.listFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.TransactionDetailActivity;
import amarnehsoft.com.debits.beans.CustomTransaction;
import amarnehsoft.com.debits.constants.PaymentMethod;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.MyColors;

/**
 * Created by jcc on 8/18/2017.
 */

public class TransactionListFragment extends ListFragment  {
    private long FromDate = 0;
    private long ToDate = 0;
    public static final String ARG_PERSON_CODE="personCode";

    private boolean Orderd = false;
    private int OrderType = 1; // 1-> by date, 2-> by price, 3-> by name
    public static Fragment newInstance(int numberOfCols,int type,String personCode){
        Fragment fragment = new TransactionListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        bundle.putInt(ARG_TYPE,type);
        bundle.putString(ARG_PERSON_CODE,personCode);
        bundle.putInt(ARG_TYPE_TOOLS,ADVANCED_TOOLS);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void OBPriceClick() {
        OrderType = 2;
        Orderd = !Orderd;
        UpdateRecyclerAndList();
    }

    @Override
    protected void OBDateClick() {
        OrderType = 1;
        Orderd = !Orderd;
        UpdateRecyclerAndList();
    }

    @Override
    protected void OBNameClick() {
        OrderType = 3;
        Orderd = !Orderd;
        UpdateRecyclerAndList();
    }

    @Override
    protected void OnDateClick(int reqCode, long TimeInMillis) {
        if(reqCode == 1) {
            FromDate = TimeInMillis;
        }else if(reqCode == 2) {
            ToDate = TimeInMillis;
        }
        UpdateRecyclerAndList();
    }

    private Adapter mAdapter;

    @Override
    public void setupRecyclerView(){
//        List<CustomTransaction> list = getItems(mQuery);
//
//        UpdateRecycler(list);
        UpdateRecyclerAndList();
    }
    public void UpdateRecycler(List<CustomTransaction> list){
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

        if(getPersonCode()  != null)
            return;
        mQuery = query;
        UpdateRecyclerAndList();
    }

    private String getPersonCode(){
        return getArguments().getString(ARG_PERSON_CODE);
    }

    private int getType(){
        return getArguments().getInt(ARG_TYPE,-1);
    }



    private void UpdateRecyclerAndList() {
        List<CustomTransaction> transactions = TransactionsDB.getInstance(getContext()).getCustomTransactionsWithTools(getType(),mQuery,getPersonCode()
                ,FromDate,ToDate,OrderType,Orderd);
        UpdateRecycler(transactions);
    }

    private class Holder extends ListFragment.Holder<CustomTransaction>
    {
        private CustomTransaction mItem;
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

        public void bind(CustomTransaction item)
        {
            mItem = item;
            String paymentMethod ="";
            if(mItem.getType() == 1){
                paymentMethod =" (" + PaymentMethod.get(mItem.getPaymentMethod()).getString(getContext()) + ")";
            }
            dateTextView.setText(DateUtils.formatDate(mItem.getTransDate()) + paymentMethod);
            priceTextView.setText(mItem.getAmount() + " " + mItem.getCurName());

            personTextView.setText(mItem.getPersonName());

            if(mItem.getType() == 0)
                priceTextView.setTextColor(MyColors.debitColor);
            else
                priceTextView.setTextColor(MyColors.paymentColor);

//            Cur cur = CurDB.getInstance(getContext()).getBeanById(mItem.getCurCode());
//            String curName = getString(R.string.not_found);
//            if (cur != null) curName = cur.getName();
            curTextView.setText("");
        }

        @Override
        public void onClicked(View v)
        {
            Log.e("Amarneh","trans Code = "+mItem.getCode());
            startActivityForResult(TransactionDetailActivity.getIntent(getContext(),mItem.getCode()),REQ_EDIT);
        }
    }

    private class Adapter extends ListFragment.Adapter<CustomTransaction>
    {
        public Adapter(List<CustomTransaction> items) {
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
