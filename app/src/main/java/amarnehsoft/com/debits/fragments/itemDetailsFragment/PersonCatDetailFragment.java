package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import amarnehsoft.com.debits.R;
import amarnehsoft.com.debits.activities.itemDetailActivities.ItemDetailActivity;
import amarnehsoft.com.debits.beans.Person;
import amarnehsoft.com.debits.beans.PersonCat;
import amarnehsoft.com.debits.db.PersonCatsDB;
import amarnehsoft.com.debits.db.PersonsDB;
import amarnehsoft.com.debits.fragments.listFragments.ListFragment;
import amarnehsoft.com.debits.fragments.listFragments.PersonsListFragment;
import amarnehsoft.com.debits.fragments.tabedFragments.TransactionsTabedFragment;

public class PersonCatDetailFragment extends ItemDetailFragment<PersonCat> {

    public static ItemDetailFragment newInstance(String catCode){
        ItemDetailFragment fragment = new PersonCatDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailActivity.ARG_ITEM_ID,catCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PersonCatDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBean();
    }

    private void setBean(){
        if (getArguments().containsKey(ItemDetailActivity.ARG_ITEM_ID)) {
            mItem = PersonCatsDB.getInstance(getContext()).getBeanById(getArguments().getString(ItemDetailActivity.ARG_ITEM_ID));
        }else {
            mItem=null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persen_cat_details, container, false);

        if (mItem != null) {

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, PersonsListFragment.newInstance(1,mItem.getCode(), ListFragment.MODE_VIEW))
                    .commit();

        }

        return rootView;
    }

    @Override
    protected void refreshView() {
        //nothing to refresh
    }
}
