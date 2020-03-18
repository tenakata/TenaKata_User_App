package com.tenakata.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tenakata.Adapters.HomeViewPagerAdapter;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.HomeModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.FontFamily;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.SnapHelperOneByOne;
import com.tenakata.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;

public class FragmentHome extends BaseFragment {
    private Context context;
    private FragmentHomeBinding binding;
    private LinearLayoutManager horizontalLayoutManager;

    private PieData pieData;
    private PieDataSet pieDataSet;
    private ArrayList pieEntries;
    private ProgressDialog progressDialog;
    private  CircleIndicator2 indicator;
    private HomeViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        progressDialog = new ProgressDialog(context);


        binding.radioButtonSale.setChecked(true);
        indicator = view.findViewById(R.id.indicator);
        getCircularProgressChart();
        chartData();
        hitApi("sales");
        binding.viewActivity.setText("Sales Activity");
        //binding.radioGroup.setOnCheckedChangeListener(this);

        horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(horizontalLayoutManager);
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        binding.recyclerView.setOnFlingListener(null);
        linearSnapHelper.attachToRecyclerView(binding.recyclerView);
        indicator.attachToRecyclerView(binding.recyclerView, linearSnapHelper);


        binding.radioButtonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.viewActivity.setText("Sales Activity");
                binding.recyclerView.smoothScrollToPosition(0);
            }
        });


        binding.radioButtonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.viewActivity.setText("Purchase Activity");
                binding.recyclerView.smoothScrollToPosition(1);
            }
        });

        binding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                int visiblePosition = horizontalLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (visiblePosition > -1)  {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (visiblePosition == 0){
                            binding.viewActivity.setText("Sales Activity");
                            hitApi("sales");
                            binding.radioButtonSale.setChecked(true);
                            binding.radioButtonPurchase.setChecked(false);

                        }else {
                            binding.viewActivity.setText("Purchase Activity");
                            hitApi("purchase");
                            binding.radioButtonSale.setChecked(false);
                            binding.radioButtonPurchase.setChecked(true);
                        }
                    }


                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
    }


    private void getCircularProgressChart() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(3f, 0));
        pieEntries.add(new PieEntry(4f, 1));

        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.animateXY(1000, 1000);
        binding.pieChart.setTouchEnabled(false);


        binding.pieChart.setEntryLabelColor(Color.BLUE);

        binding.pieChart.getLegend().setEnabled(false);
        pieData.setDrawValues(false);
        binding.pieChart.getDescription().setEnabled(false);

        //binding.pieChart.setTransparentCircleRadius(100f);
        binding.pieChart.setHoleRadius(80f);

        //pieDataSet.setSelectionShift(20);


        pieDataSet.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.coloOrange));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTypeface(FontFamily.getPtRegular());
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setSliceSpace(1f);

    }

    private void chartData() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(20f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(19f, 5));


        BarDataSet set1 = new BarDataSet(entries, "");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);

        binding.barchart.setData(data); // set the data and list of labels into chart
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        binding.barchart.animateXY(1000, 1000);


        binding.barchart.setDrawBarShadow(false);
        binding.barchart.setDrawValueAboveBar(false);
        binding.barchart.getDescription().setEnabled(false);
        binding.barchart.setPinchZoom(false);
        binding.barchart.setDrawGridBackground(false);


        YAxis yAxis = binding.barchart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setGranularity(1f);
        yAxis.setGranularityEnabled(false);
        binding.barchart.getAxisRight().setEnabled(false);

        XAxis xAxis = binding.barchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        /*LimitLine ll = new LimitLine(40, "Systolic range");
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(40 - 20);

        ll.setTextColor(Color.WHITE);
        ll.setTextSize(12f);

        binding.barchart.getAxisLeft().setDrawLimitLinesBehindData(true);*/


        binding.barchart.setFitBars(true);

        Legend l = binding.barchart.getLegend();

        l.setEnabled(false);
        l.setFormSize(12f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTextSize(10f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f);


        binding.barchart.getAxisLeft().setDrawGridLines(false);
        binding.barchart.getAxisRight().setDrawGridLines(false);

        binding.barchart.getAxisLeft().setDrawLabels(false);
        binding.barchart.getAxisLeft().setAxisLineColor(Color.TRANSPARENT);
        binding.barchart.getAxisRight().setDrawLabels(false);
        binding.barchart.getAxisLeft().setZeroLineColor(getResources().getColor(R.color.colorBlue));



        /*final String[] quarters = new String[]{"Q1", "Q2", "Q3", "Q4","Q5","Q6"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = binding.barchart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);*/
    }

    private void hitApi(String sales_purchases) {

        if (!progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        }

        String url = HRUrlFactory.getBaseUrl().concat(HRAppConstants.URL_HOME);
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", "purchase");
            jsonObject.put("filter", "month");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.objectApi(context, url, this, jsonObject, HRUrlFactory.getAppHeaders(), false);



        /*final HashMap<String,String> jsonObject = new HashMap<>();
        try {

            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", sales_purchases);
            jsonObject.put("filter", "month");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Authentication.post(url,  jsonObject,this, true);*/
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        if (progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.dismiss();
        }
        if (responseObj instanceof HomeModel) {
            HomeModel model = (HomeModel) responseObj;
            binding.viewAveragePrice.setText(HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal_average())));
            binding.viewCashSales.setText("Cash Sales KES " + HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCash_amount())));
            binding.viewCashPurchase.setText("Cash Purchase KES " + HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCredit_amount())));

            List<String> l = new ArrayList<>();
            l.add("Sales");
            l.add("Purchase");

            if (adapter == null) {
                adapter = new HomeViewPagerAdapter(l, context,
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCash_amount())),
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCredit_amount())));
                binding.recyclerView.setAdapter(adapter);
            }else {
                 adapter.refresh(l,
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCash_amount())),
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCredit_amount())));
            }
        }
    }

    @Override
    public void onTaskError(String errorMsg) {
        if (progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.dismiss();
        }
        ErrorDialog.errorDialog(context, getString(R.string.app_name), errorMsg);
    }
}
