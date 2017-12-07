package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ItemDetailActivity;
import amarnehsoft.com.debits.beans.Cur;
import amarnehsoft.com.debits.db.sqlite.CurDB;

public class CurDetailFragment extends ItemDetailFragment<Cur> {

    private TextView equ_text_view;

    public static ItemDetailFragment newInstance(String curCode){
        ItemDetailFragment fragment = new CurDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailActivity.ARG_ITEM_ID,curCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CurDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBean();
    }

    private void setBean(){
        if (getArguments().containsKey(ItemDetailActivity.ARG_ITEM_ID)) {
            mItem = CurDB.getInstance(getContext()).getBeanById(getArguments().getString(ItemDetailActivity.ARG_ITEM_ID));
        }else {
            mItem=null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cur_details, container, false);
        equ_text_view = (TextView)rootView.findViewById(R.id.equ_text_view);

        if (mItem != null) {
            refreshView();
        }

        return rootView;
    }

    @Override
    protected void refreshView() {
        equ_text_view.setText(mItem.getEqu()+"");
    }
}
