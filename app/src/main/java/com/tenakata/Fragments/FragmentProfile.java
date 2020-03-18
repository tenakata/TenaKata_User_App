package com.tenakata.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.tenakata.Base.BaseFragment;
import com.tenakata.R;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.FragmentProfileBinding;

public class FragmentProfile extends BaseFragment {
    private Context context;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        if (HRPrefManager.getInstance(context).getUserDetail().getResult()!=null){

            binding.viewUserName.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));
            binding.tvUserid.setText("#TANA"+HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getId()));
            binding.tvMobile.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getPhone()));
        }
    }
}
