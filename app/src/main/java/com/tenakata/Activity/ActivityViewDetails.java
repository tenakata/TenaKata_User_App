package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tenakata.Adapters.HomeSalesBaseAdapter;
import com.tenakata.Base.BaseActivity;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.Dialog.DialogRemindPayment;
import com.tenakata.Dialog.showPayDialog;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.Models.HomeModel;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Models.PayAmountModel;
import com.tenakata.Models.ViewDetailsModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.ActivityCreditViewDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDetails extends BaseActivity implements
        showPayDialog.onAmountPaid,
        DialogRemindPayment.ReminderCallBack {
    ActivityCreditViewDetailsBinding binding;
    static String dueamount;
    static String payingamount;
    int position;
    Context context;
    private List<ViewDetailsModel.ResultBean> list = new ArrayList<>();
    private Intent intent;


    @Override
    public void onClick(int viewId, View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_credit_view_details);

        context = this;
        intent = getIntent();
        binding.viewRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemindDialog(intent.getStringExtra("id"), binding.tvAmount.getText().toString());
            }
        });
        binding.viewPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayDialog(intent.getStringExtra("id"), binding.tvAmount.getText().toString());
            }
        });


        try {
            apiHit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (intent.getStringExtra("payment_type").equals("cash")) {
            binding.viewPayBtn.setVisibility(View.GONE);
            binding.viewRemindBtn.setVisibility(View.GONE);
        }

        if (intent.getStringExtra("sales_purchases").equals("purchase") && intent.getStringExtra("payment_type").equals("credit")) {
            binding.viewRemindBtn.setVisibility(View.GONE);
        }
    }


    public void showPayDialog(String id, String totalAmount) {
        new showPayDialog(context, id, totalAmount, this);
    }

    public void showRemindDialog(String id, String totalAmount) {
        new DialogRemindPayment(context, totalAmount, this, id);

    }


    public void hitRemindApi(String Id, String message) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("transaction_id", Id);
            jsonObject.put("sales_purchases", "sales");
            jsonObject.put("message", message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.objectApi(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_REMIND),
                this, jsonObject, HRUrlFactory.getAppHeaders(), true);

    }


    private void apiHit() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", intent.getStringExtra("id"));
        jsonObject.put("sales_purchases", intent.getStringExtra("sales_purchases"));
        jsonObject.put("payment_type", intent.getStringExtra("payment_type"));


        if (intent.getStringExtra("sales_purchases").equals("purchase")) {
            Log.e("yoooooo", intent.getStringExtra("id"));
            Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_PURCHASEVIEWDETAIL), this, jsonObject);
        }
        if (intent.getStringExtra("sales_purchases").equals("sales")) {
            Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_SALEVIEWDETAIL), this, jsonObject);

        }


    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        if (responseObj instanceof ViewDetailsModel) {
            ViewDetailsModel model = (ViewDetailsModel) responseObj;
            if (model.getResult().size() > 0) {
                String path = (model.getResult().get(0).getAttach_recepit());
                Glide.with(this)
                        .load(path)
                        .apply(new RequestOptions()
                                .transform(new RoundedCorners(20)).placeholder(R.drawable.offer_sel))
                        .into(binding.imageView11);
                String amount = (HRPriceFormater.roundDecimalByTwoDigits(Double.parseDouble(model.getResult().get(0).getAmount())));
                binding.tvAmount.setText(amount);
                binding.tvItemList.setText(model.getResult().get(0).getItem_list());
                binding.tvCreditviewdetailsHead.setText(model.getResult().get(0).getName());
            }
        } else if (responseObj instanceof PayAmountModel) {
            double a = Double.parseDouble(payingamount);
            double b = Double.parseDouble(binding.tvAmount.getText().toString());
            double c = b - a;
            String currentamount = (HRPriceFormater.roundDecimalByTwoDigits(c));
            binding.tvAmount.setText(currentamount);
            super.onTaskSuccess(responseObj);
        } else if (responseObj instanceof ModelSuccess) {
            Toast.makeText(this, "Reminded Successsfully", Toast.LENGTH_LONG).show();
            dismissLoader();
        }
    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }

    @Override
    public void onAmountPaidByUser(String id, String amount, String mode, String narration) {
        hitPayApi(id, amount, mode, narration);
    }

    private void hitPayApi(String id, String amount, String mode, String narration) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("transaction_id", id);
            jsonObject.put("amount_paid", amount);
            payingamount = amount;
            jsonObject.put("payment_option", mode);
            jsonObject.put("sales_purchases", intent.getStringExtra("sales_purchases"));
            jsonObject.put("naration", narration);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.objectApi(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_PAY_AMOUNT),
                this, jsonObject, HRUrlFactory.getAppHeaders(), true);
    }

    @Override
    public void onRemind(String Id, String message) {
        hitRemindApi(Id, message);

    }
}
