package com.app.chendurfincorp.client.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.app.chendurfincorp.client.tab.Investment;
import com.app.chendurfincorp.client.tab.Today;
import com.app.chendurfincorp.client.tab.Total;

public class HomeTabAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public HomeTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Today tab1 = new Today();
                return tab1;
            case 1:
                Investment tab2 = new Investment();
                return tab2;
            case 2:
                Total tab3 = new Total();
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
