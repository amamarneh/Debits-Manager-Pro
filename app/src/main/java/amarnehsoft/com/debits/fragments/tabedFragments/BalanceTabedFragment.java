package amarnehsoft.com.debits.fragments.tabedFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.fragments.listFragments.BalanceListFragment;

/**
 * Created by jcc on 8/19/2017.
 */


public class BalanceTabedFragment extends TabedFragment{

    public static final String ARG_SELECTED_TAB="selectedTab";

    public static BalanceTabedFragment newInstance(int selectedTab) {
        BalanceTabedFragment frag = new BalanceTabedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_TAB,selectedTab);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Fragment getFragment(int type) {
        return BalanceListFragment.newInstance(2,type);
    }
}

