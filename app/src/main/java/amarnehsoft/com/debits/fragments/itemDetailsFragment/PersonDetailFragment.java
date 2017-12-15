package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ItemDetailActivity;
import amarnehsoft.com.debits.activities.listActivities.TransactionListActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.controllers.BalancesController;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.utils.MyColors;
import amarnehsoft.com.debits.utils.NumberUtils;
import amarnehsoft.com.debits.utils.WhatsAppUtils;

public class PersonDetailFragment extends ItemDetailFragment<Person> {

    private TextView mAddressTxtView,mPhoneTextView,mEmailTextView,mBalanceMeTextView , mBalanceOnMeTextView,mTxtBalance;
    private View btnShowAll,btnShowMe,btnShowOnMe,phoneImg,balanceLayout;
    View mPhonelayout,mMaillayout,mAddresslayout,mPhoneView,mMailView;
    public static ItemDetailFragment newInstance(String personCode){
        ItemDetailFragment fragment = new PersonDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailActivity.ARG_ITEM_ID,personCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PersonDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPerson();
    }

    private void setPerson(){
        if (getArguments().containsKey(ItemDetailActivity.ARG_ITEM_ID)) {
            mItem = PersonsDB.getInstance(getContext()).getBeanById(getArguments().getString(ItemDetailActivity.ARG_ITEM_ID));
        }else {
            mItem=null;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persen_details, container, false);
        mPhonelayout = rootView.findViewById(R.id.phone_layout);
        mMaillayout = rootView.findViewById(R.id.mail_layout);
        mAddresslayout = rootView.findViewById(R.id.address_layout);
        mPhoneView = rootView.findViewById(R.id.phone_layout_view);
        mMailView = rootView.findViewById(R.id.mail_layout_view);
        mPhoneTextView = (TextView) rootView.findViewById(R.id.phone_text_view);
        mEmailTextView = (TextView) rootView.findViewById(R.id.email_text_view);
        mBalanceMeTextView = (TextView) rootView.findViewById(R.id.txtMe);
        mAddressTxtView = (TextView)rootView.findViewById(R.id.address_text_view);
        mBalanceOnMeTextView = (TextView) rootView.findViewById(R.id.txtOnMe);
        mTxtBalance = (TextView)rootView.findViewById(R.id.txtBalance);
        btnShowMe = rootView.findViewById(R.id.debitsBtn);
        btnShowOnMe = rootView.findViewById(R.id.paymentsBtn);
        btnShowAll = rootView.findViewById(R.id.txtShowAll);
        phoneImg = rootView.findViewById(R.id.phoneImg);
        balanceLayout = rootView.findViewById(R.id.balanceLayout);

        mBalanceMeTextView.setTextColor(MyColors.debitColor);
        mBalanceOnMeTextView.setTextColor(MyColors.paymentColor);

        if (mItem != null) {
            btnShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(TransactionListActivity.getIntent(getContext(),1,-1,mItem.getKey()));
                }
            });

            btnShowMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(TransactionListActivity.getIntent(getContext(),1,0,mItem.getKey()));
                }
            });
            btnShowOnMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(TransactionListActivity.getIntent(getContext(),1,1,mItem.getKey()));
                }
            });


            refreshView();
        }

        balanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, WhatsAppUtils.generateContent(getContext(),mItem));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        rootView.findViewById(R.id.btnWhatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WhatsAppUtils.send(mItem.getPhone(),WhatsAppUtils.generateContent(getContext(),mItem));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private double getBalance(int type){
        switch (type){
            case 0: //
            case 1: // Payments Or Debits for single Person
                return TransactionsDB.getInstance(getContext()).getTotalTransaction(mItem.getKey(),type);
            case -1: // total Balance for single person
                return BalancesController.getTotalBalance(getContext(),mItem.getKey());
        }
        return 0;

//        Balance result = BalancesController.getBalancesForPerson(getContext(),type,mItem.getKey());
//        double amount = 0;
//        amount = result.getAmount();
//        return amount; //balance
    }

    @Override
    public void refreshView() {
        setPerson();
        if(!TextUtils.isEmpty(mItem.getPhone())){
            mPhoneTextView.setText(mItem.getPhone());
            mPhonelayout.setVisibility(View.VISIBLE);

        }else {
            mPhonelayout.setVisibility(View.GONE);

        }

        if(!TextUtils.isEmpty(mItem.getEmail())){
            mEmailTextView.setText(mItem.getEmail());
            mMaillayout.setVisibility(View.VISIBLE);
            mPhoneView.setVisibility(View.VISIBLE);
        }else {
            mMaillayout.setVisibility(View.GONE);
            mPhoneView.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(mItem.getAddress())){
            mAddressTxtView.setText(mItem.getAddress());
            mAddresslayout.setVisibility(View.VISIBLE);
            mMailView.setVisibility(View.VISIBLE);
        }else {
            mAddresslayout.setVisibility(View.GONE);
            mMailView.setVisibility(View.GONE);
        }
        Cur cur = CurDB.getInstance(getContext()).getDefualtBean();
        String balanceMe = NumberUtils.Round(getBalance(0))+" " + cur.getName();
        mBalanceMeTextView.setText(balanceMe);
        String balanceOnMe = Math.abs(NumberUtils.Round(getBalance(1)))+" " + cur.getName();
        mBalanceOnMeTextView.setText(balanceOnMe);

        double balance = NumberUtils.Round(getBalance(-1));
        if(balance > 0) // Setup Balance Color
            mTxtBalance.setTextColor(MyColors.debitColor);
        else
            mTxtBalance.setTextColor(MyColors.paymentColor);
        String sBalance = Math.abs(balance) + "";
        mTxtBalance.setText(sBalance);

    }
}
