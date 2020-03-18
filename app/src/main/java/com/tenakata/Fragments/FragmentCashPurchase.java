package com.tenakata.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quentindommerc.superlistview.OnMoreListener;
import com.tenakata.Activity.ActivityAddDailySales;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Base.BaseActivity;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.FragmentCashPurchaseBinding;
import com.tenakata.databinding.FragmentCashSalesBinding;

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
    private HomeSalesBaseAdapter adapter;

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

        binding.viewAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivityAddDailySales.class)
                        .putExtra("sales_purchases","purchase")
                );
            }
        });


    }


    private void hitApi(boolean isEnable) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", currentPage);
            jsonObject.put("Perpage", per_page);
            jsonObject.put("payment_type", "cash");
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", "purchase");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.searchFilterApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_CASH_CREDIT_Purchase),
                this, jsonObject, HRUrlFactory.getAppHeaders(), isEnable);
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
        hitApi(false);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        binding.listItem.removeMoreListener();
        if (list.size() > 0) list.clear();
        hitApi(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        hitApi(true);
    }


    @Override
    public void onPayClick(String id, String totalAmount) {

    }

    @Override
    public void onRemindClick(String id, String totalAmount) {

    }

    @Override
    public void onViewDetailsClick(int id) {

    }

}