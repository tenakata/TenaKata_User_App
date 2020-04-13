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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.tenakata.Adapters.GraphSpinnerAdapter;
import com.tenakata.Adapters.HomeViewPagerAdapter;
import com.tenakata.Application.App;
import com.tenakata.Base.BaseFragment;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.GraphModel;
import com.tenakata.Models.HomeModel;
import com.tenakata.Network.Authentication;
import com.tenakata.Network.ResponseParser;
import com.tenakata.R;
import com.tenakata.Utilities.FontFamily;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.SnapHelperOneByOne;
import com.tenakata.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator2;

import static com.tenakata.Network.Authentication.print;

public class FragmentHome extends BaseFragment implements HomeViewPagerAdapter.Callback {
    private Context context;
    private FragmentHomeBinding binding;
    private LinearLayoutManager horizontalLayoutManager;
        private String sales_purchase;
    private PieData pieData,pieData2;
    private PieDataSet pieDataSet,pieDataSet2;
    private ArrayList pieEntries,pieEntriespurchase;
    private ProgressDialog progressDialog;
    private CircleIndicator2 indicator;
    private HomeViewPagerAdapter adapter;
    private CallBackAgain callBackAgain;
    private ArrayList<String> graphLabelList = new ArrayList<>();
    private ArrayList<Float> graphAmountList = new ArrayList<>();
    private int flag = 0;
    private String type = "day";

    public FragmentHome(CallBackAgain callBackAgain) {
        super();
        this.callBackAgain = callBackAgain;
    }

    public FragmentHome(Runnable runnable) {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        getCircularProgressChart("1", "1");
        hitApi("sales", type);
       hitGraphApi("sales", type);
        binding.viewActivity.setText("Sales Activity");
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
                if (visiblePosition > -1) {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (visiblePosition == 0) {
                            binding.viewActivity.setText("Sales Activity");
                            hitApi("sales", type);
                            hitGraphApi("sales", type);
                            binding.radioButtonSale.setChecked(true);
                            binding.radioButtonPurchase.setChecked(false);
                            if (type != null && !type.equals("")) {
                                if (type.equalsIgnoreCase("day")) {
                                    binding.apinner.setSelection(0);
                                }
                               else if (type.equalsIgnoreCase("week")) {
                                    binding.apinner.setSelection(1);
                                } else if (type.equalsIgnoreCase("month")) {
                                    binding.apinner.setSelection(2);
                                } else if (type.equalsIgnoreCase("year")) {
                                    binding.apinner.setSelection(3);
                                }
                            }

                        } else {
                            binding.viewActivity.setText("Purchase Activity");
                            hitApi("purchase", type);
                            hitGraphApi("purchase", type);

                            binding.radioButtonSale.setChecked(false);
                            binding.radioButtonPurchase.setChecked(true);
                            if (type != null && !type.equals("")) {
                                if (type.equalsIgnoreCase("day")) {
                                    binding.apinner.setSelection(0);
                                }
                                else if (type.equalsIgnoreCase("week")) {
                                    binding.apinner.setSelection(1);
                                } else if (type.equalsIgnoreCase("month")) {
                                    binding.apinner.setSelection(2);
                                } else if (type.equalsIgnoreCase("year")) {
                                    binding.apinner.setSelection(3);
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        spinner();
    }

    private void spinner() {

        final String[] typeArray = {"daily","weekly", "monthly", "yearly"};
        GraphSpinnerAdapter adapter = new GraphSpinnerAdapter(context, typeArray);
        binding.apinner.setAdapter(adapter);

        binding.apinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    TextView textView = (TextView) view.findViewById(R.id.tvLanguageName);
                    textView.setTextColor(context.getResources().getColor(R.color.colorWhite));

                    if (++flag > 1) {
                        switch (binding.apinner.getSelectedItemPosition()) {
                            case 0:
                                type = "day";
                                hitGraphApi(sales_purchase, "day");
                                hitApi(sales_purchase, "day");
                                break;
                            case 1:
                                type = "week";
                                hitGraphApi(sales_purchase, "week");
                                hitApi(sales_purchase, "week");
                                break;
                            case 2:
                                type = "month";
                                hitGraphApi(sales_purchase, "month");
                                hitApi(sales_purchase, "month");
                                break;
                            case 3:
                                type = "year";
                                hitGraphApi(sales_purchase, "year");
                                hitApi(sales_purchase, "year");
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getCircularProgressChart(String cash, String credit) {

        if ((cash.equals("0.0")||cash.equals("0")  ||cash.equals("0.00"))&& (credit.equals("0.0")||credit.equals("0")||credit.equals("0.00"))){
            pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(1f, 0));
            pieEntries.add(new PieEntry(1f, 1));
        }else {
            pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(Float.parseFloat(cash), 0));
            pieEntries.add(new PieEntry(Float.parseFloat(credit), 1));
        }


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

        if (Double.parseDouble(cash)>0 && Double.parseDouble(credit)>0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(cash)==0 && Double.parseDouble(credit)>0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(cash)>0 && Double.parseDouble(credit)==0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorGray));
        }else {
            pieDataSet.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
        }


        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setSliceSpace(1f);





    }

    private void chartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < graphLabelList.size(); i++) {
            entries.add(new BarEntry((float) i, graphAmountList.get(i)));
        }

        BarDataSet set1 = new BarDataSet(entries, "");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.2f);


        binding.barchart1.setData(data); // set the data and list of labels into chart
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        binding.barchart1.animateXY(1000, 1000);

        binding.barchart1.setDrawBarShadow(false);
        binding.barchart1.setDrawValueAboveBar(false);
        binding.barchart1.getDescription().setEnabled(false);
        binding.barchart1.setPinchZoom(false);
        binding.barchart1.setDrawGridBackground(false);

        YAxis yAxis = binding.barchart1.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setGranularity(1f);
        yAxis.setGranularityEnabled(false);
        binding.barchart1.getAxisRight().setEnabled(false);

        XAxis xAxis = binding.barchart1.getXAxis();
        xAxis.setGranularity(10f);
        xAxis.setGranularityEnabled(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        /*LimitLine ll = new LimitLine(40, "Systolic range");
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(40 - 20);
        ll.setTextColor(Color.WHITE);
        ll.setTextSize(12f);
        binding.barchart1.getAxisLeft().setDrawLimitLinesBehindData(true);*/

        binding.barchart1.setFitBars(true);
        Legend l = binding.barchart1.getLegend();
        l.setEnabled(false);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTextSize(10f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f);

        binding.barchart1.getAxisLeft().setDrawGridLines(false);
        binding.barchart1.getAxisRight().setDrawGridLines(false);
        binding.barchart1.getAxisLeft().setDrawLabels(false);
        binding.barchart1.getAxisRight().setDrawLabels(false);
        binding.barchart1.getAxisLeft().setAxisLineColor(Color.TRANSPARENT);
        binding.barchart1.getAxisLeft().setZeroLineColor(getResources().getColor(R.color.colorBlue));

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    return graphLabelList.get(Math.round(value));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return "Time";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Time";
                }

            }
        };
        XAxis xAxis1 = binding.barchart1.getXAxis();
        xAxis1.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis1.setValueFormatter(formatter);

       // binding.barchart1.getXAxis().setLabelRotationAngle(3f);
        binding.barchart1.getXAxis().setTextSize(8);
        /*binding.barchart1.getXAxis().setAxisMinimum(0.1f);
        binding.barchart1.setExtraLeftOffset(10f);*/

       // binding.barchart1.setVisibleXRangeMaximum(graphLabelList.size());
      //  binding.barchart1.setVisibleXRangeMinimum(5);
     //   binding.barchart1.moveViewToX(graphAmountList.size());
      //  binding.barchart1.fitScreen();


        binding.barchart1.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        binding.barchart1.getXAxis().setTextColor(Color.WHITE);
        binding.barchart1.getLegend().setTextColor(Color.WHITE);
    }

    private void hitApi(String sales_purchases, String type) {
       this.sales_purchase= sales_purchases;
        if (!progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        }
        String url = HRUrlFactory.getBaseUrl().concat(HRAppConstants.URL_HOME);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", sales_purchases);
            jsonObject.put("filter", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_HOME), this, jsonObject);
    }

    private void hitGraphApi(String sales_purchases, String type) {
        String url = HRUrlFactory.getBaseUrl().concat(HRAppConstants.URL_GRAPH);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", sales_purchases);
            jsonObject.put("filter", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        graphApi(url, jsonObject, HRUrlFactory.getAppHeaders());
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        if (progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.dismiss();
        }
        if (responseObj instanceof HomeModel) {
            HomeModel model = (HomeModel) responseObj;

            binding.viewAveragePrice.setText("KES " + HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal_average())));
            binding.viewCashSales.setText("Cash "+sales_purchase+" KES "+HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCash_amount())));
            binding.viewCashPurchase.setText("Credit "+sales_purchase+" KES "+ HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCredit_amount())));
            getCircularProgressChart((HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCash_amount())), HRPriceFormater.roundDecimalByTwoDigits(model.getResult().getCredit_amount()));



            binding.viewCashPurchase2.setText(model.getPercentage()+"%");

            if (model.getArraow()){
                binding.upDownLogo.setImageResource(R.drawable.greenup);
            }
            else if (model.getArraow().equals(false)){
                binding.upDownLogo.setImageResource(R.drawable.reddown);
            }
            else {
                binding.upDownLogo.setImageResource(R.drawable.eqqual);
            }

            List<String> l = new ArrayList<>();
            l.add("Sales");
            l.add("Purchase");

            if (adapter == null) {
                adapter = new HomeViewPagerAdapter(l, context,
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal())),
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal())), this);
                binding.recyclerView.setAdapter(adapter);
            } else {
                adapter.refresh(l,

                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal())),
                        HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal())));
            }
        } else if (responseObj instanceof GraphModel) {
            GraphModel model = (GraphModel) responseObj;
        }
    }


    @Override
    public void onTaskError(String errorMsg) {
        if (progressDialog.isShowing() && !((Activity) context).isFinishing()) {
            progressDialog.dismiss();
        }
        ErrorDialog.errorDialog(context, getString(R.string.app_name), errorMsg);
    }

    @Override
    public void onSaleClick() {
        callBackAgain.onSaleClick();
    }

    @Override
    public void onPurchaseClick() {
        callBackAgain.onPurchaseClick();
    }

    public interface CallBackAgain {
        void onSaleClick();

        void onPurchaseClick();
    }

    private void graphApi(final String url,
                          final JSONObject jsonObject,
                          final HashMap<String, String> headers) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {

                        if (graphLabelList.size() > 0) graphLabelList.clear();
                        if (graphAmountList.size() > 0) graphAmountList.clear();
                        try {
                            HRPrefManager.getInstance(context).setStringValue("GRAPH_DATA", response.toString());
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Iterator<String> keys = object.keys();
                                if (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    Log.d("==========>Dkeyynm", "-> " + key);
                                    String value = object.getString(key);
                                    if (HRValidationHelper.isNull(value)){
                                        value="0";
                                        Log.d("==========>DDDnmnm", "-> " + value);
                                    }
                                    Log.d("==========>DDD", "-> " + value);

                                    if (jsonObject.getString("filter").equalsIgnoreCase("day")) {
                                        graphLabelList.add(HRPriceFormater.simplegraphWeekFormater(value));
                                    }
                                    else if (jsonObject.getString("filter").equalsIgnoreCase("week")) {
                                        graphLabelList.add(value);
                                    }else if (jsonObject.getString("filter").equalsIgnoreCase("month")) {
                                        graphLabelList.add(HRPriceFormater.changeSimpleMonthFormate(value));
                                    } else {
                                        graphLabelList.add(value);
                                    }
                                }

                                if (object.has("amount")) {
                                    if (HRValidationHelper.isNull((object.getString("amount")))){
                                        graphAmountList.add((float) 0.0);

                                    }
                                    else {
                                        double amount = object.getDouble("amount");
                                        Log.d("==========>DDD", "-> " + String.valueOf(amount));
                                        graphAmountList.add((float) amount);
                                    }




                                }


                            }
                            chartData();
                        } catch (JsonIOException e) {
                            e.printStackTrace();
                        }


                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        onTaskError(App.getInstance().getString(R.string.txt_went_wrong));

                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }
}
