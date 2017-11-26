package amarnehsoft.com.debits.fragments.tabedFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import amarnehsoft.com.debits.fragments.listFragments.ReminderListFragment;

/**
 * Created by jcc on 8/19/2017.
 */

public class ReminderTabedFragment extends TabedFragment{

    public static Fragment newInstance(int selectedTab) {
        Fragment frag = new ReminderTabedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_TAB,selectedTab);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public Fragment getFragment(int type) {
        return ReminderListFragment.newInstance(1,type,null);
    }
}
