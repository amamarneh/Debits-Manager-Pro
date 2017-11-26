package amarnehsoft.com.debits.fragments.tabedFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.fragments.listFragments.TransactionListFragment;

/**
 * Created by jcc on 8/19/2017.
 */

public class TransactionsTabedFragment extends TabedFragment{

    public static final String ARG_PERSON_CODE = "personCode";

    public static Fragment newInstance(int selectedTab,String personCode) {
        //personCode : null -> show for all persons , :not null -> show for this person
        Fragment frag = new TransactionsTabedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_TAB,selectedTab);
        bundle.putString(ARG_PERSON_CODE,personCode);
        frag.setArguments(bundle);
        return frag;
    }

    private String getPersonCode(){
        return getArguments().getString(ARG_PERSON_CODE);
    }
    @Override
    public Fragment getFragment(int type) {
        return TransactionListFragment.newInstance(1,type,getPersonCode());
    }
}
