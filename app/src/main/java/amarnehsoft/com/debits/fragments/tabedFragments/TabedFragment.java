package amarnehsoft.com.debits.fragments.tabedFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amarnehsoft.com.debits.R;

/**
 * Created by jcc on 8/19/2017.
 */


public abstract class TabedFragment extends Fragment{

    public static final String ARG_SELECTED_TAB="selectedTab";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabed, container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getDialog().setTitle(getString(R.string.add_new_person));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.sym_def_app_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.sym_def_app_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(android.R.drawable.sym_def_app_icon));

        final ViewPager viewPager = (ViewPager)view.findViewById(R.id.pager);
        final PagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        tabLayout.getTabAt(getSelectedTab()).select();
    }

    private int getSelectedTab(){
        return getArguments().getInt(ARG_SELECTED_TAB,0);
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {

        int tabCount;

        public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
            super(fm);
            this.tabCount = numberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            if (position < tabCount){
                return getFragment(position-1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    public abstract Fragment getFragment(int type);
}

