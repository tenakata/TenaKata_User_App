package com.tenakata.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tenakata.Fragments.FragmentCashPurchase;
import com.tenakata.Fragments.FragmentCreditPurchase;

public class HomePurchageViewPagerAdapter  extends FragmentPagerAdapter {
    private Context context;
    int tabs;

    public HomePurchageViewPagerAdapter(FragmentManager fragment, Context context, int tabs) {
        super(fragment);
        this.context = context;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentCashPurchase();
        } else {
            return new FragmentCreditPurchase();
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}