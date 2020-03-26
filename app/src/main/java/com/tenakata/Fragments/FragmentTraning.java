package com.tenakata.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tenakata.Activity.ActivityTrainingDetails;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Adapters.TrainingBaseAdapter;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Models.TrainingListModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.databinding.FragmentTrainingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTraning extends BaseFragment implements TrainingBaseAdapter.RowClick{

    private Context context;
    private FragmentTrainingBinding binding;
    List<TrainingListModel.ResultBean> list = new ArrayList<>();
    private TrainingBaseAdapter adapter;
    private int currentPage = 1;
    private String per_page = "10";

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
         hitApi();
    }

    private void hitApi() {

        final JSONObject jsonObject = new JSONObject();
        try {



            jsonObject.put("page", "1");
            jsonObject.put("Perpage", "10");
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("role", "user");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.object(getActivity(), HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING),this,jsonObject);
    }


      @Override
    public void onTaskSuccess(Object responseObj) {
        dismissLoader();
        if (!(responseObj instanceof TrainingListModel)) {
            return;
        }
        TrainingListModel model = (TrainingListModel) responseObj;


        if (list.size() > 0) list.clear();

        if (model.getResult().size() > 0) {
            list.addAll(model.getResult());
        }
        if (adapter == null) {
            adapter = new TrainingBaseAdapter(getActivity(),list,this);
            binding.listItem.setAdapter(adapter);
        } else {
          //  adapter.refresh(list);
        }
    }


    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }

    @Override
    public void onRowClick(int position,String user_id) {
       // Toast.makeText(getActivity(),String.valueOf(user_id),Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(), ActivityTrainingDetails.class);
        intent.putExtra("id",user_id);
        startActivity(intent);

    }
}
