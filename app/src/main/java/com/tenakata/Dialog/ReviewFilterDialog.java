package com.tenakata.Dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tenakata.R;
import com.tenakata.databinding.ShortLayoutBinding;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReviewFilterDialog implements View.OnClickListener {
    private Context context;
    private BottomSheetDialog mBottomSheetDialog;
    private ShortLayoutBinding binding;
    private ProgressDialog progressDialog;
    private String selectedDay="amount";
    private String selectedCarID;
    private FilterApplyClick filterApplyClick;
    String filterOrSort;
    private int mYear, mDay, mMonth;
    private String startString, endString;
    String selected="";
    int count=0;

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
            binding.startDate.setText("Start Date");
            binding.endDate.setText("End Date");
            binding.endDate.setVisibility(View.VISIBLE);
            binding.filterlayout.setVisibility(View.VISIBLE);







        }

        binding.view30.setOnClickListener(this);
        binding.view90.setOnClickListener(this);
        binding.startDate.setOnClickListener(this);
        binding.endDate.setOnClickListener(this);
        binding.image30.setVisibility(View.VISIBLE);

        binding.viewApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()){
                    try {

                        if (filterApplyClick!=null){
                            filterApplyClick.onFilterApplyClick(selectedDay);
                            dismissRideDialog();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private boolean validation() {
        if (selected.equals("startend")){
                if (startString!=null && endString !=null){
                    selectedDay="'"+startString+"'"+" and "+"'"+endString+"'";
                    return true;
                }
                else if (startString==null || endString==null){
                    Toast.makeText(context,"Enter Date",Toast.LENGTH_SHORT).show();
                    return false;

            }
            else {

                return false;
            }
        }
        else {
            return true;
        }

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
                    binding.imagestartDate.setVisibility(View.GONE);
                    binding.startDate.setText("Start Date");
                    binding.endDate.setText("End Date");
                    selectedDay="15";
                    selected="15";
                    count=0;
                    startString=null;
                    endString=null;
                    break;

                case R.id.view90:
                    binding.image90.setVisibility(View.VISIBLE);
                    binding.image30.setVisibility(View.GONE);

                    binding.imagestartDate.setVisibility(View.GONE);
                    binding.startDate.setText("Start Date");
                    binding.endDate.setText("End Date");
                    selectedDay="30";
                    count=0;
                    selected="30";
                    startString=null;
                    endString=null;
                    break;
                case R.id.startDate :
                    binding.image90.setVisibility(View.GONE);
                    binding.image30.setVisibility(View.GONE);

                    binding.imagestartDate.setVisibility(View.VISIBLE);
                    binding.startDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calender("startDate");
                        }
                    });
                    count++;
                    selected="startend";
                    break;
                case R.id.endDate :
                    binding.image90.setVisibility(View.GONE);
                    binding.image30.setVisibility(View.GONE);
                    binding.imagestartDate.setVisibility(View.VISIBLE);
                    binding.endDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calender("endDate");
                        }
                    });
                count++;
                    selected="startend";

                break;
            }
        }




    }



    private void calender(final String startOrEnd) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        mYear = year;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePicker= new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);

                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, 0);
                if (startOrEnd.equals("startDate")){
                    binding.startDate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());

                    startString=DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                }else if(startOrEnd.equals("endDate")){
                    binding.endDate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());
                    endString=DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                }



            }
        },mYear,mMonth,mDay);
        datePicker.show();
    }

    public interface FilterApplyClick{
        void onFilterApplyClick(String type);

    }
}
