package com.tenakata.Activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tenakata.Base.BaseActivity;
import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.ModelOtp;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRLogger;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityMobileNumberBinding;

import org.json.JSONObject;

import java.util.HashMap;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityVerifyMobileNumber extends BaseActivity  {
    private ActivityMobileNumberBinding binding;
    private Context context;
    private String countryCode;

    @Override
    public void onClick(int viewId, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile_number);
        context = this;

        binding.ccp.registerPhoneNumberTextView(binding.viewMobile);
        countryCode = binding.ccp.getSelectedCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
            }
        });
        progressDialog = new ProgressDialog(context);

        binding.viewBtnSubmit.setOnClickListener(this);

        binding.viewMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    binding.ccp.registerPhoneNumberTextView(binding.viewMobile);
                    binding.textInputLayout.setHint("Mobile Number");
                }else{
                    binding.viewMobile.setHint("Mobile Number");
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewBtnSubmit:
                if (isValidate(view)){
                    apiCheckUser();
                }
                break;

        }
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        if (!isFinishing()) progressDialog.dismiss();
        if (responseObj instanceof ModelOtp) {
            ModelOtp modelSuccess =(ModelOtp)responseObj;
            startActivity(IntentHelper.getOtpVerification(context)
                    .putExtra("contact",binding.viewMobile.getText().toString())
                    .putExtra("countryCode",countryCode).putExtra("otp",modelSuccess.getOtp()));
            finish();
        }
    }

    @Override
    public void onTaskError(String errorMsg) {
        if (!isFinishing()) progressDialog.dismiss();
        ErrorDialog.errorDialog(context, getString(R.string.app_name), errorMsg);
    }



    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    private boolean isValidate(View view) {
        if (HRValidationHelper.isNull(binding.viewMobile.getText().toString())) {
            HRLogger.showSneckbar(binding.parent,getString(R.string.txt_enter_mobile_number));
            return false;
        }else if (!binding.ccp.isValid()) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_valid_mobile));
            return false;
        }else if (HRValidationHelper.isNull(countryCode)) {
            HRLogger.showSneckbar(binding.parent, getString(R.string.txt_select_country_code));
            return false;
        }
        return true;
    }

    private void apiCheckUser(){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject params=new JSONObject();
        try {
            params.put("phone", binding.viewMobile.getText().toString());
            params.put("country_code", countryCode);
            params.put("role", "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_OTP),this,params);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

