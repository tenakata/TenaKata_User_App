package com.tenakata.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tenakata.Fragments.FragmentCreditSales;
import com.tenakata.Fragments.FragmentCashSales;

public class CashFlowPagerAdapter extends FragmentPagerAdapter {
    private int tabs;

    public CashFlowPagerAdapter(FragmentManager fragment, int tabs) {
        super(fragment);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentCashSales();
        } else if (position == 1){
            return new FragmentCreditSales();
        }else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
