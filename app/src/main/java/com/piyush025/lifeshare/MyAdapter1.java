package com.piyush025.lifeshare;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter1  extends FragmentPagerAdapter {

    Context myContext;
    int totalTabs;

    public MyAdapter1(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                DonateRequest tab1 = new DonateRequest();
                return tab1;
            case 1:
                ReceiveRequest tab2= new ReceiveRequest();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
