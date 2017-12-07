package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ItemDetailActivity;
import amarnehsoft.com.debits.beans.Bank;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.Transaction;
import amarnehsoft.com.debits.constants.PaymentMethod;
import amarnehsoft.com.debits.db.sqlite.BanksDB;
import amarnehsoft.com.debits.db.sqlite.CurDB;
import amarnehsoft.com.debits.db.sqlite.PersonsDB;
import amarnehsoft.com.debits.db.sqlite.TransactionsDB;
import amarnehsoft.com.debits.utils.DateUtils;
import amarnehsoft.com.debits.utils.MyColors;

public class TransactionDetailFragment extends ItemDetailFragment<Transaction> {

    private TextView person_text_view, transDate_text_view, amount_text_view, cur_text_view, creationDate_text_view, notes_text_view,
                    txtPaymentType,txtBankName,txtCheckNum,txtCheckDate;

    public static ItemDetailFragment newInstance(String tranCode){
        ItemDetailFragment fragment = new TransactionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailActivity.ARG_ITEM_ID,tranCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TransactionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBean();
    }

    private void setBean(){
        if (getArguments().containsKey(ItemDetailActivity.ARG_ITEM_ID)) {
            mItem = TransactionsDB.getInstance(getContext()).getBeanById(getArguments().getString(ItemDetailActivity.ARG_ITEM_ID));
        }else {
            mItem=null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        person_text_view = (TextView)rootView.findViewById(R.id.person_text_view);
        transDate_text_view = (TextView)rootView.findViewById(R.id.transDate_text_view);
        amount_text_view = (TextView)rootView.findViewById(R.id.amount_text_view);
        cur_text_view = (TextView)rootView.findViewById(R.id.cur_text_view);
        creationDate_text_view=  (TextView)rootView.findViewById(R.id.creationDate_text_view);
        notes_text_view = (TextView)rootView.findViewById(R.id.notes_text_view);
        txtPaymentType = (TextView)rootView.findViewById(R.id.txtPaymentMethod);
        txtBankName = (TextView)rootView.findViewById(R.id.txtBank);
        txtCheckNum = (TextView)rootView.findViewById(R.id.txtCheckNum);
        txtCheckDate = (TextView)rootView.findViewById(R.id.txtCheckDate);

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
        person_text_view.setText(personName);
        transDate_text_view.setText(DateUtils.formatDate( mItem.getTransDate()));
        amount_text_view.setText(mItem.getAmount()+"");

        amount_text_view.setTextColor(mItem.getType() == 0? MyColors.debitColor:MyColors.paymentColor);

        Cur cur = CurDB.getInstance(getContext()).getBeanById(mItem.getCurCode());
        String curName = getString(R.string.not_found);
        if (cur != null) curName = cur.getName();
        cur_text_view.setText(curName);

        creationDate_text_view.setText(DateUtils.formatDate( mItem.getCreationDate()));
        notes_text_view.setText(mItem.getNotes());

        if (mItem.getType() == 1){
            PaymentMethod method = PaymentMethod.get(mItem.getPaymentMethod());
            txtPaymentType.setText(method.getString(getContext()));
            if (method == PaymentMethod.CHECK){
                String bankName ="";
                Bank bank = BanksDB.getInstance(getContext()).getBeanById(mItem.getBankCode());
                if (bank != null) bankName = bank.getName();
                txtBankName.setText(bankName);
                txtCheckNum.setText(mItem.getCheckNum());
                txtCheckDate.setText(DateUtils.formatDateWithoutTime(mItem.getCheckDate()));

                txtBankName.setVisibility(View.VISIBLE);
                txtCheckDate.setVisibility(View.VISIBLE);
                txtCheckNum.setVisibility(View.VISIBLE);
            }else {
                txtBankName.setVisibility(View.GONE);
                txtCheckNum.setVisibility(View.GONE);
                txtCheckDate.setVisibility(View.GONE);
            }
        }else {
            txtPaymentType.setVisibility(View.GONE);
            txtBankName.setVisibility(View.GONE);
            txtCheckNum.setVisibility(View.GONE);
            txtCheckDate.setVisibility(View.GONE);
        }
    }
}
