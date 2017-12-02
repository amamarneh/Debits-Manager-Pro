package amarnehsoft.com.debits.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import amarnehsoft.com.debits.Dummy;
import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.Services.FloatingViewService;
import amarnehsoft.com.debits.activities.listActivities.BalanceListActivity;
import amarnehsoft.com.debits.activities.listActivities.BanksListActivity;
import amarnehsoft.com.debits.activities.listActivities.CurListActivity;
import amarnehsoft.com.debits.activities.listActivities.ListActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsActivity;
import amarnehsoft.com.debits.activities.listActivities.PersonsCatListActivity;
import amarnehsoft.com.debits.activities.listActivities.ReminderListActivity;
import amarnehsoft.com.debits.activities.listActivities.TransactionListActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.db.BanksDB;
import amarnehsoft.com.debits.db.CurDB;
import amarnehsoft.com.debits.db.PersonCatsDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.db.TransactionsDB;
import amarnehsoft.com.debits.fragments.dialogs.LanguagesDialogFragment;
import amarnehsoft.com.debits.utils.MyColors;
import amarnehsoft.com.debits.utils.NumberUtils;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final String TAG_PERSONS="PERSONS";
    public static final String TAG_DEBTS="debts";
    public static final String TAG_PAYMENTS ="payments";
    public static final String TAG_PERSONS_CAT="personsCat";
    public static final String TAG_BALANCES="balances";
    public static final String TAG_RECIEPTS_DATE="recieptDate";
    public static final String TAG_CUR = "cur";
    private static final String TAG_BANKS="banks";

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static Intent newIntent(Context ctx , int numberOfCols){
        Intent intent = new Intent(ctx,MainActivity.class);
        intent.putExtra(ARG_NUM_OF_COLS,numberOfCols);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        getIntent().putExtra(ARG_NUM_OF_COLS,2);
        setupRecyclerView();
    }


    private List<DashboardItem> getItems(){
        DashboardItem personsItem = new DashboardItem(TAG_PERSONS,getResources().getDrawable(R.drawable.ic_person_black_48dp),getString(R.string.thePersons));
        DashboardItem personsCatItem = new DashboardItem(TAG_PERSONS_CAT,getResources().getDrawable(R.drawable.ic_credit_card_black_36dp),getString(R.string.personsCat));
        DashboardItem debitsItem = new DashboardItem(TAG_DEBTS,getResources().getDrawable(R.drawable.debits),getString(R.string.theDebits));
        DashboardItem paymentsItem = new DashboardItem(TAG_PAYMENTS,getResources().getDrawable(R.drawable.payments),getString(R.string.thePayments));
        DashboardItem balancesItem = new DashboardItem(TAG_BALANCES,getResources().getDrawable(R.drawable.bal),getString(R.string.theBalances));
//        DashboardItem datesItem = new DashboardItem(TAG_RECIEPTS_DATE,getResources().getDrawable(R.drawable.ic_alarm_black_36dp),getString(R.string.recieptsDates));
        DashboardItem curItem = new DashboardItem(TAG_CUR,getResources().getDrawable(R.drawable.ic_monetization_on_black_48dp),getString(R.string.theCurs));
        DashboardItem banksItem = new DashboardItem(TAG_BANKS,getResources().getDrawable(R.drawable.ic_monetization_on_black_48dp),getString(R.string.theBanks));


        int personsCount = PersonsDB.getInstance(this).getNoOfBeans();
        int catCount = PersonCatsDB.getInstance(this).getNoOfBeans();
        int curCount = CurDB.getInstance(this).getNoOfBeans();
        double debitsSum = TransactionsDB.getInstance(this).getSumOfTransaction(0);
        double paymentsSum = TransactionsDB.getInstance(this).getSumOfTransaction(1);
        double balanceSum = debitsSum - paymentsSum;
        String balanceType= balanceSum>=0?getString(R.string.debits):getString(R.string.credits);
        balanceSum = Math.abs(balanceSum);
        Cur cur = CurDB.getInstance(this).getDefualtBean();
        personsItem.setSummery( personsCount + " " + getString(R.string.persons));
        personsCatItem.setSummery( catCount+ " " + getString(R.string.cats));
        debitsItem.setSummery(NumberUtils.Round(debitsSum) + " " + cur.getName());
        paymentsItem.setSummery( NumberUtils.Round(paymentsSum) + " " + cur.getName());
        balancesItem.setSummery( NumberUtils.Round(balanceSum) + " " + cur.getName() + " " + balanceType);
        curItem.setSummery( curCount + " " + getString(R.string.Curs));
        int banksCount = BanksDB.getInstance(this).getNoOfBeans();
        banksItem.setSummery(banksCount + " " + getString(R.string.banks));

        debitsItem.setLineColor(MyColors.debitColor);
        paymentsItem.setLineColor(MyColors.paymentColor);
        balancesItem.setLineColor(balanceSum>=0?MyColors.debitColor:MyColors.paymentColor);


        List<DashboardItem> result = new ArrayList<>();

        result.add(balancesItem);
        result.add(debitsItem);
        result.add(paymentsItem);
        result.add(personsItem);
//        result.add(datesItem);
        result.add(curItem);
        result.add(personsCatItem);
        result.add(banksItem);
        return result;
    }

    private void setupRecyclerView(){
        int numberOfCols = getIntent().getIntExtra(ARG_NUM_OF_COLS,1);
        final int [] spanArr = new int[]{2,1,1,1,1,1,1};
        if(numberOfCols == 1)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        else {
            GridLayoutManager layoutManager = new GridLayoutManager(this,numberOfCols);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return spanArr[position];
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
        }

        setupListAndRecycler();
    }
    private void setupListAndRecycler(){
        List<DashboardItem> list = getItems();
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
    protected void onResume() {
        super.onResume();
        setupListAndRecycler();
    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView mImageView;
        private TextView mTextView;
        private TextView mSummeryTextView;
        private DashboardItem mDashboardItem;
        private View lineView;
        public Holder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            mImageView= (ImageView)itemView.findViewById(R.id.imageView);
            mTextView=(TextView)itemView.findViewById(R.id.textView);
            mSummeryTextView=(TextView)itemView.findViewById(R.id.summeryTextView);
            lineView= itemView.findViewById(R.id.lineView);
        }


        public void bind(DashboardItem item)
        {
            mDashboardItem = item;
            mTextView.setText(item.getTitle());
            mImageView.setImageDrawable(item.getDrawable());
            mSummeryTextView.setText(item.getSummery());
            lineView.setBackgroundColor(item.getLineColor());
        }

        @Override
        public void onClick(View v)
        {
            switch (mDashboardItem.getTag()){
                case TAG_PERSONS:
                    startActivity(PersonsActivity.getIntent(MainActivity.this,1, ListActivity.MODE_VIEW));
                    break;
                case TAG_PERSONS_CAT:
                    startActivity(PersonsCatListActivity.getIntent(MainActivity.this,1,ListActivity.MODE_VIEW));
                    break;
                case TAG_DEBTS:
                    startActivity(TransactionListActivity.getIntent(MainActivity.this,1,0,null));
                    break;
                case TAG_PAYMENTS:
                    startActivity(TransactionListActivity.getIntent(MainActivity.this,1,1,null));
                    break;
                case TAG_BALANCES:
                    startActivity(BalanceListActivity.getIntent(MainActivity.this,1,2));
                    break;
                case TAG_RECIEPTS_DATE:
                    startActivity(ReminderListActivity.getIntent(MainActivity.this,1,-1));
                    break;
                case TAG_CUR:
                    startActivity(CurListActivity.getIntent(MainActivity.this,1,ListActivity.MODE_VIEW));
                    break;
                case TAG_BANKS:
                    startActivity(BanksListActivity.getIntent(MainActivity.this,1,ListActivity.MODE_VIEW));
                    break;
            }
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder>
    {
        private List<DashboardItem> mItems;

        public Adapter(List<DashboardItem> items)
        {
            mItems = items;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.row_dashboard_item_layout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
            DashboardItem item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
        public void setList(List<DashboardItem> items){
            mItems=items;
        }

    }

    public static final String ARG_NUM_OF_COLS = "numberOfCols";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.menu_calculator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


                //If the draw over permission is not available open the settings screen
                //to grant the permission.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            } else {
                startService(new Intent(this, FloatingViewService.class));
                //finish();
            }
            return true;
        }

        if(id == R.id.menu_language){
            LanguagesDialogFragment.newInstance().show(getSupportFragmentManager(),LanguagesDialogFragment.TAG);
        }

        return super.onOptionsItemSelected(item);
    }


    private void MakeToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private class DashboardItem{
        private Drawable mDrawable;
        private String title;
        private String tag;
        private String summery;

        public int getLineColor() {
            return lineColor;
        }

        public void setLineColor(int lineColor) {
            this.lineColor = lineColor;
        }

        private int lineColor;

        public String getSummery() {
            return summery;
        }

        public void setSummery(String summery) {
            this.summery = summery;
        }

        public DashboardItem(String tag, Drawable drawable, String title) {
            this.tag = tag;
            mDrawable = drawable;
            this.title = title;
            summery = "";
            lineColor = Color.TRANSPARENT;
        }

        public String getTag(){
            return tag;
        }
        public void setTag(String tag){
            this.tag = tag;
        }

        public Drawable getDrawable() {
            return mDrawable;
        }

        public void setDrawable(Drawable drawable) {
            mDrawable = drawable;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}


