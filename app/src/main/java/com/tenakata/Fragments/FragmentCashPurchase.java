package com.tenakata.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quentindommerc.superlistview.OnMoreListener;
import com.tenakata.Activity.ActivityAddDailySales;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Base.BaseActivity;
import android.widget.Toast;

import com.quentindommerc.superlistview.OnMoreListener;
import com.tenakata.Activity.ActivityAddDailySales;
import com.tenakata.Activity.ActivityViewDetails;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Dialog.ReviewFilterDialog;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.FragmentCashPurchaseBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCashPurchase extends BaseFragment implements OnMoreListener,
        SwipeRefreshLayout.OnRefreshListener, HomeSalesBaseAdapter.RowClick{
    private FragmentCashPurchaseBinding binding;
    private Context context;
    private int currentPage = 1;
    private String per_page = "10";
    private boolean isSorting;
    private String sorting_type;
    private HomeSalesBaseAdapter adapter;
    private boolean isFilter;

    private List<CashSalesCreditModel.ResultBean> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_cash_purchase, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();

        binding.listItem.setRefreshListener(this);

        if (FragmentPurchaseFlow.viewFilter != null) {
            FragmentPurchaseFlow.viewFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ReviewFilterDialog(context, new ReviewFilterDialog.FilterApplyClick() {
                        @Override
                        public void onFilterApplyClick(String type) {
                            isFilter=true;
                            sorting_type= type;
                            currentPage = 1;
                            hitApi(true,false,type,true);
                        }
                    },"filter");

                }
            });
        }

        if (FragmentPurchaseFlow.viewSort != null) {
            FragmentPurchaseFlow.viewSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ReviewFilterDialog(context, new ReviewFilterDialog.FilterApplyClick() {
                        @Override
                        public void onFilterApplyClick(String type) {
                            isSorting = true;
                            sorting_type= type;
                            currentPage = 1;
                            Log.i("tooo","sfhsofhoshfsdjf");
                            hitApi(true,true,sorting_type,false);
                        }
                    },"sort");
                }
            });
        }

        binding.viewAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivityAddDailySales.class)
                        .putExtra("sales_purchases","purchase").putExtra("title","Add Daily Cash Purchase").putExtra("defaultradiobutton","cash")
                );
            }
        });


    }






    private void hitApi(boolean isEnable,boolean isSorting,String sorting_type,boolean isFilter) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", currentPage);
            jsonObject.put("Perpage", per_page);
            jsonObject.put("payment_type", "cash");
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", "purchase");
            if (isSorting && sorting_type!=null){
                jsonObject.put("sorting_type", sorting_type);
            }
            if (isFilter && sorting_type!=null){
                jsonObject.put("sorting_type", sorting_type);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isFilter){
            Authentication.searchFilterApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_FILTER),
                    this, jsonObject, HRUrlFactory.getAppHeaders(), isEnable);
        }else

        if (isSorting){
            Authentication.searchFilterApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_SORTING),
                    this, jsonObject, HRUrlFactory.getAppHeaders(), isEnable);
        }else {
            Authentication.searchFilterApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_CASH_CREDIT_SALES),
                    this, jsonObject, HRUrlFactory.getAppHeaders(), isEnable);
        }
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
       dismissLoader();
        if (!(responseObj instanceof CashSalesCreditModel)) {
            return;
        }
        CashSalesCreditModel model = (CashSalesCreditModel) responseObj;

        binding.viewTotalSale.setText("KES".concat(" ").concat(HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal_amount()))));

        if (list.size() > 0) list.clear();
        if (model.getResult().size() > 0) {
            list.addAll(model.getResult());
        }
        if (adapter == null) {
            adapter = new HomeSalesBaseAdapter(context, list,this,"cashPurchase");
            binding.listItem.setAdapter(adapter);
        } else {
            adapter.refresh(list);
        }
        if (list != null && list.size() > 9) {
            binding.listItem.setupMoreListener(this, 0);
            if (binding.listItem.isLoadingMore()) {
                binding.listItem.setLoadingMore(false);
            }
        } else {
            currentPage = 1;
            binding.listItem.removeMoreListener();
        }
    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
        dismissLoader();
    }

    @Override
    public void onLoadMore(Object responseObj) {
        dismissLoader();
        if (!(responseObj instanceof CashSalesCreditModel)) {
            return;
        }
        try {
            CashSalesCreditModel model = (CashSalesCreditModel) responseObj;
            binding.listItem.hideMoreProgress();
            adapter.onMoreDataReq(model.getResult());
            if (binding.listItem.isLoadingMore()) {
                binding.listItem.setLoadingMore(false);
            }
            binding.listItem.removeMoreListener();
            if (model.getResult().size() == 0) {
                binding.listItem.removeMoreListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        currentPage = currentPage + 1;
        if (isSorting){

            hitApi(false,true,sorting_type,false);
        }else if (isFilter){

            hitApi(false,false,sorting_type,true);
        }
        else {

            hitApi(false,false,null,false);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        binding.listItem.removeMoreListener();
        if (list.size() > 0) list.clear();
        isSorting = false;
        isFilter=false;

        hitApi(true,false,null,false);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        isSorting = false;
        isFilter=false;

        hitApi(true,false,null,false);

    }


    @Override
    public void onPayClick(String id, String totalAmount) {

    }

    @Override
    public void onRemindClick(String id, String totalAmount) {

    }

    @Override
    public void onViewDetailsClick(int position ,String id, String name, String receiptpath , String amount,String list) {

        Intent intent=new Intent(getActivity(), ActivityViewDetails.class);
        intent.putExtra("id",id);
        intent.putExtra("sales_purchases","purchase");
        intent.putExtra("payment_type","cash");
        Toast.makeText(getActivity(),id,Toast.LENGTH_LONG).show();
        startActivity(intent);

    }





}
