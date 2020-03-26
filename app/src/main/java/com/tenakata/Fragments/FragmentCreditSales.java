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
import android.widget.Toast;

import com.quentindommerc.superlistview.OnMoreListener;
import com.tenakata.Activity.ActivityAddDailySales;
import com.tenakata.Activity.ActivityViewDetails;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Dialog.DialogRemindPayment;
import com.tenakata.Dialog.ReviewFilterDialog;
import com.tenakata.Dialog.showPayDialog;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Models.PayAmountModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.FragmentCreditSalesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreditSales extends BaseFragment implements OnMoreListener,
        SwipeRefreshLayout.OnRefreshListener, HomeSalesBaseAdapter.RowClick, showPayDialog.onAmountPaid, DialogRemindPayment.ReminderCallBack {

    private FragmentCreditSalesBinding binding;
    private Context context;
    private int currentPage = 1;
    private String per_page = "10";
    private HomeSalesBaseAdapter adapter;
    private boolean isSorting;
    private String sorting_type;
    private List<CashSalesCreditModel.ResultBean> list = new ArrayList<>();
    private boolean isFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_credit_sales, container, false);
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
                        .putExtra("sales_purchases","sales").putExtra("title","Add new Credit Sale").putExtra("defaultradiobutton","credit")
                );
            }
        });

        if (FragmentCashFlow.viewFilter1!=null) {
            FragmentCashFlow.viewFilter1.setOnClickListener(new View.OnClickListener() {
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

        if (FragmentCashFlow.viewSort1!=null) {
            FragmentCashFlow.viewSort1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ReviewFilterDialog(context, new ReviewFilterDialog.FilterApplyClick() {
                        @Override
                        public void onFilterApplyClick(String type) {
                            isSorting = true;
                            sorting_type= type;
                            currentPage = 1;
                            hitApi(true,true,type,false);
                        }
                    },"sort");
                }
            });
        }

    }

    private void hitApi(boolean isEnable,boolean isSorting,String sorting_type,boolean isFilter) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", currentPage);
            jsonObject.put("Perpage", per_page);
            jsonObject.put("payment_type", "credit");
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("sales_purchases", "sales");
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
        if ((responseObj instanceof CashSalesCreditModel)) {
            CashSalesCreditModel model = (CashSalesCreditModel) responseObj;
            binding.viewTotalSale.setText("KES".concat(" ").concat(HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(model.getTotal_amount()))));

            if (list.size() > 0) list.clear();
            if (model.getResult().size() > 0) {
                list.addAll(model.getResult());
            }
            if (adapter == null) {
                adapter = new HomeSalesBaseAdapter(context, list, this, "crediSale");
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
        }else if (responseObj instanceof PayAmountModel){
            onRefresh();
        }else if (responseObj instanceof ModelSuccess){
            Toast.makeText(context,"Reminder Send Successfully",Toast.LENGTH_SHORT).show();
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
    public void onTaskError(String errorMsg) {
        dismissLoader();
    }

    @Override
    public void onPayClick(String id,String totalAmount) {
        new showPayDialog(context,id,totalAmount,this);
    }

    @Override
    public void onRemindClick(String id,String totalAmount) {
        new DialogRemindPayment(context,totalAmount,this,id);

    }

    @Override
    public void onViewDetailsClick(int position,String id,String name,String receiptpath,String amount,String list) {
        Intent intent=new Intent(getActivity(), ActivityViewDetails.class);
        intent.putExtra("id",id);
        intent.putExtra("position",String.valueOf(position));
        intent.putExtra("sales_purchases","sales");
        intent.putExtra("payment_type","credit");
        startActivity(intent);
    }


    @Override
    public void onAmountPaidByUser(String id, String amount, String mode, String narration) {
            hitPayApi(id,amount,mode,narration);
    }

    private void hitPayApi(String id, String amount, String mode, String narration){

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("transaction_id", id);
            jsonObject.put("amount_paid", amount);
            jsonObject.put("payment_option", mode);
            jsonObject.put("sales_purchases", "sales");
            jsonObject.put("naration", narration);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.objectApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_PAY_AMOUNT),
                this, jsonObject, HRUrlFactory.getAppHeaders(), true);
    }

    @Override
    public void onRemind(String Id, String message) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("transaction_id", Id);
            jsonObject.put("sales_purchases", "sales");
            jsonObject.put("message", message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.objectApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_REMIND),
                this, jsonObject, HRUrlFactory.getAppHeaders(), true);

    }
}
