package com.tenakata.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tenakata.Adapters.HomePurchageViewPagerAdapter;
import com.tenakata.R;
import com.tenakata.Utilities.FontFamily;
import com.tenakata.databinding.FragmentPurchaseFlowBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPurchaseFlow extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentPurchaseFlowBinding binding;
    private HomePurchageViewPagerAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        binding.toolbarBackView.setOnClickListener(this);
        setViewPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_purchase_flow, container, false);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbarBackView:
                //finish();
                break;

            default:
                break;
        }
    }

    private void setViewPage() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.txt_cash_purchase)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.txt_credit_purchase)));
        adapter = new HomePurchageViewPagerAdapter(getChildFragmentManager(), context, binding.tabLayout.getTabCount());
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        changeTabsFont();
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) binding.tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontFamily.getMonRegular());
                    ((TextView) tabViewChild).setTextSize(12);
                }
            }
        }
    }

}
