package com.app.chendurfincorp.client.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.chendurfincorp.client.tab.InvestmentFragment;
import com.app.chendurfincorp.client.tab.TodayFragment;
import com.app.chendurfincorp.client.tab.TotalFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TodayFragment tab1 = new TodayFragment();
                return tab1;
            case 1:
                InvestmentFragment tab2 = new InvestmentFragment();
                return tab2;
            case 2:
                TotalFragment tab3 = new TotalFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
