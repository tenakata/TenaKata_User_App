package com.tenakata.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.tenakata.Adapters.TrainingBaseAdapter;
import com.tenakata.Base.BaseFragment;
import com.tenakata.R;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.FragmentTrainingBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentTraning extends BaseFragment implements TrainingBaseAdapter.RowClick {

    private Context context;
    private FragmentTrainingBinding binding;
    List<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_training, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        list.add("q");
        list.add("w");
        list.add("e");
        list.add("e");
        list.add("r");
        list.add("r");

        binding.videoList.setAdapter(new TrainingBaseAdapter(context,list,this));
    }

    @Override
    public void onRowClick(int id) {
        startActivity(IntentHelper.getTrainingVideo(getActivity()));

    }
}
