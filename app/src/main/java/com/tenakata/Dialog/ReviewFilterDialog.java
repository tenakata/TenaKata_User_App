package com.tenakata.Dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tenakata.R;
import com.tenakata.databinding.ShortLayoutBinding;

import org.json.JSONObject;

public class ReviewFilterDialog implements View.OnClickListener {
    private Context context;
    private BottomSheetDialog mBottomSheetDialog;
    private ShortLayoutBinding binding;
    private ProgressDialog progressDialog;
    private String selectedDay="amount";
    private String selectedCarID;
    private FilterApplyClick filterApplyClick;
    String filterOrSort;

    public ReviewFilterDialog(Context context,FilterApplyClick callBack,String filterOrSort ){
        this.filterOrSort=filterOrSort;
        this.context = context;
        progressDialog=new ProgressDialog(context);
        this.filterApplyClick=callBack;
        showRideDialog();
    }

    public void showRideDialog() {

        mBottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.short_layout, null, false);
        mBottomSheetDialog.setContentView(binding.getRoot());

        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.show();

        binding.viewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissRideDialog();
            }
        });

        binding.viewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissRideDialog();
            }
        });

        if (filterOrSort.equals("filter")){
            selectedDay="15";
            binding.view30.setText("15 Days");
            binding.view90.setText("30 Days");

        }

        binding.view30.setOnClickListener(this);
        binding.view90.setOnClickListener(this);
        binding.image30.setVisibility(View.VISIBLE);

        binding.viewApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                if (filterApplyClick!=null){
                    filterApplyClick.onFilterApplyClick(selectedDay);
                    dismissRideDialog();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dismissRideDialog() {
        if (!((Activity) context).isFinishing() && mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (filterOrSort.equals("sort")){
            switch (v.getId()){
                case R.id.view30:
                    binding.image90.setVisibility(View.GONE);
                    binding.image30.setVisibility(View.VISIBLE);
                    selectedDay="amount";

                    break;

                case R.id.view90:
                    binding.image90.setVisibility(View.VISIBLE);
                    binding.image30.setVisibility(View.GONE);
                    selectedDay="date";
                    break;
            }
        }
        else if (filterOrSort.equals("filter")){
            switch (v.getId()){
                case R.id.view30:
                    binding.image90.setVisibility(View.GONE);
                    binding.image30.setVisibility(View.VISIBLE);
                    selectedDay="15 Days";

                    break;

                case R.id.view90:
                    binding.image90.setVisibility(View.VISIBLE);
                    binding.image30.setVisibility(View.GONE);
                    selectedDay="30 Days";
                    break;
            }
        }


    }

    public interface FilterApplyClick{
        void onFilterApplyClick(String type);

    }
}
