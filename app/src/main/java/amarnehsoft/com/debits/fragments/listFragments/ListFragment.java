package amarnehsoft.com.debits.fragments.listFragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.beans.Balance;
import amarnehsoft.com.debits.fragments.dialogs.DatePickerFragment;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.MyColors;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */

public abstract class ListFragment extends Fragment implements DatePickerFragment.IDatePickerFragment{

    public static final String ARG_NUM_OF_COLS = "numberOfCols";
    public static final String ARG_MODE="mode";
    public static final String ARG_TYPE= "type";
    public static final String ARG_TYPE_TOOLS= "typetools";
    public static final int REQ_EDIT = 1;

    public static final int MODE_VIEW=0;
    public static final int MODE_SEARCH=1;

    public static final int NON_TOOLS = 1;
    public static final int BASIC_TOOLS = 2;
    public static final int ADVANCED_TOOLS = 3;

    protected int mMode;
    protected String mQuery;

    protected Button orderbynameBtn,orderbydateBtn,orderbypriceBtn;
    protected View fromdateBtn,todateBtn,moreBtn,datesearchLayout,orderbyTextView;
    private TextView fromdateTextView,todateTextView;
    private Switch fromdateSwitch,todateSwitch;
    protected RecyclerView mRecyclerView;

    private long FromDate = 0;
    private long ToDate = 0;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMode = getArguments().getInt(ARG_MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        orderbynameBtn =(Button)  view.findViewById(R.id.orderbynameBtn);
        orderbydateBtn =(Button)  view.findViewById(R.id.orderbydateBtn);
        orderbypriceBtn = (Button) view.findViewById(R.id.orderbypriceBtn);
        fromdateBtn =  view.findViewById(R.id.fromdateBtn);
        todateBtn =  view.findViewById(R.id.todateBtn);
        fromdateTextView = (TextView) view.findViewById(R.id.fromdateTextView);
        todateTextView = (TextView) view.findViewById(R.id.todateTextView);
        fromdateSwitch = (Switch) view.findViewById(R.id.fromdateSwitch);
        todateSwitch = (Switch) view.findViewById(R.id.todateSwitch);
        moreBtn = view.findViewById(R.id.moreBtn);
        datesearchLayout= view.findViewById(R.id.datesearchLayout);
        orderbyTextView= view.findViewById(R.id.orderbyTextView);
        initRecyclerView();
        initTools();
        setupRecyclerView();
        return view;
    }

    private void initTools() {
        int type = getArguments().getInt(ARG_TYPE_TOOLS);
            switch (type){
                case NON_TOOLS:
                    orderbynameBtn.setVisibility(View.GONE);
                    orderbydateBtn.setVisibility(View.GONE);
                    orderbypriceBtn.setVisibility(View.GONE);
                    fromdateBtn.setVisibility(View.GONE);
                    todateBtn.setVisibility(View.GONE);
                    orderbyTextView.setVisibility(View.GONE);
                    moreBtn.setVisibility(View.GONE);
                    break;
                case BASIC_TOOLS:
                    orderbynameBtn.setVisibility(View.VISIBLE);
                    orderbydateBtn.setVisibility(View.GONE);
                    orderbypriceBtn.setVisibility(View.VISIBLE);
                    orderbyTextView.setVisibility(View.VISIBLE);
                    fromdateBtn.setVisibility(View.GONE);
                    todateBtn.setVisibility(View.GONE);

                    moreBtn.setVisibility(View.GONE);
                    break;
                case ADVANCED_TOOLS:
                    orderbynameBtn.setVisibility(View.VISIBLE);
                    orderbydateBtn.setVisibility(View.VISIBLE);
                    orderbypriceBtn.setVisibility(View.VISIBLE);
                    fromdateBtn.setVisibility(View.VISIBLE);
                    todateBtn.setVisibility(View.VISIBLE);
                    orderbyTextView.setVisibility(View.VISIBLE);
                    moreBtn.setVisibility(View.VISIBLE);
                    break;
            }
    initListeners();
    }

    private void initListeners(){
        orderbynameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderbynameBtn.setTextColor(MyColors.debitColor);
                orderbydateBtn.setTextColor(Color.BLACK);
                orderbypriceBtn.setTextColor(Color.BLACK);
                OBNameClick();
            }
        });
        orderbydateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderbydateBtn.setTextColor(MyColors.debitColor);
                orderbynameBtn.setTextColor(Color.BLACK);
                orderbypriceBtn.setTextColor(Color.BLACK);
                OBDateClick();
            }
        });
        orderbypriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderbypriceBtn.setTextColor(MyColors.debitColor);
                orderbydateBtn.setTextColor(Color.BLACK);
                orderbynameBtn.setTextColor(Color.BLACK);
                OBPriceClick();
            }
        });
        fromdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerFragment.newInstance(calendar,1,ListFragment.this).show(getFragmentManager(),"tag");
            }
        });
        todateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerFragment.newInstance(calendar,2,ListFragment.this).show(getFragmentManager(),"tag");
            }
        });
        todateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    OnDateClick(2,ToDate);
                else
                    OnDateClick(2,0);
            }
        });
        fromdateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    OnDateClick(1,FromDate);
                else
                    OnDateClick(1,0);
            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datesearchLayout.setVisibility(  datesearchLayout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE  );
            }
        });

    }
    @Override
    public void onDateSet(int reqCode, int year, int month, int day) {
        Calendar calender = Calendar.getInstance();
        calender.set(year,month,day,0,0,0);

        if(reqCode == 1) {
            FromDate = calender.getTimeInMillis();
            fromdateSwitch.setChecked(true);
            fromdateTextView.setText(DateUtils.formatDate(new Date(calender.getTimeInMillis())));
        }else if(reqCode == 2) {
            ToDate = calender.getTimeInMillis();
            todateSwitch.setChecked(true);
            todateTextView.setText(DateUtils.formatDate(new Date(calender.getTimeInMillis())));
        }

        OnDateClick(reqCode,calender.getTimeInMillis());
    }

    protected void OnDateClick(int reqCode, long TimeInMillis ) {

    }

    protected void OBPriceClick() {

    }

    protected void OBDateClick() {
    }

    protected void OBNameClick() {
    }

    private void initRecyclerView(){
        int numberOfCols = getNumberOfCols();
        if(numberOfCols == 1)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),numberOfCols));
    }

    private int getNumberOfCols(){
        return getArguments().getInt(ARG_NUM_OF_COLS);
    }

    public abstract void setupRecyclerView();
    public abstract void onQueryTextChanged(String query);



    public abstract class Holder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            onClicked(v);
        }

        public abstract void onClicked(View v);
        public abstract void bind(T item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT){
            if (resultCode == RESULT_OK){
                setupRecyclerView();
            }
        }
    }

    public abstract class Adapter<T> extends RecyclerView.Adapter<Holder>
    {
        private List<T> mItems;

        public Adapter(List<T> items)
        {
            mItems = items;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(getLayoutId(), parent, false);
            return getNewHolder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
            T item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
        public void setList(List<T> items){
            mItems=items;
        }

        public void clear(){
            mItems.clear();
            notifyDataSetChanged();
        }

        public abstract int getLayoutId();
        public abstract Holder getNewHolder(View v);
    }
}
