package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRLogger;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityForgotPasswordBinding;

import org.json.JSONObject;

import java.util.HashMap;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener, AuthenticationCallBacks {
    private String countryCode, contact;
    private Context context;
    private ActivityForgotPasswordBinding binding;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        context = this;
        progressDialog = new ProgressDialog(context);
        binding.viewSubmitBtn.setOnClickListener(this);

        if (getIntent() != null) {
            countryCode = getIntent().getStringExtra("countryCode");
            contact = getIntent().getStringExtra("contact");

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewSubmitBtn:
                if (validation(view)){
                    apiForgetPassword();
                }
                break;
        }
    }

    private boolean validation(View view) {

        if (HRValidationHelper.isNull(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_password));
            return false;
        }else if (!HRValidationHelper.isValidPassword(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_password_lenth_short));
            return false;

        } else if (HRValidationHelper.isNull(binding.viewConfirmPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_confirm_password));
            return false;
        } else if (!binding.viewPassword.getText().toString().equals(binding.viewConfirmPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_confirm_password_not_matching));
            return false;
        }
        return true;
    }

    private void apiForgetPassword(){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject params = new JSONObject();
        try {
            params.put("phone", contact);
            params.put("new_password", binding.viewPassword.getText().toString());
            params.put("country_code", countryCode);
            params.put("role", "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication.apiOfLogin(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_FORGOT_PASSWORD),
                this, params, HRUrlFactory.getDefaultHeaders());
    }


    @Override
    public void onSuccessCallback(Object response) {
        if (!isFinishing()) progressDialog.dismiss();
        if (response instanceof ModelSuccess) {
            ModelSuccess modelSuccess =(ModelSuccess)response;
            finish();
        }
    }

    @Override
    public void onSuccessCallback(boolean isSuccess) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onErrorCallBack(String error) {
        if (!isFinishing()) progressDialog.dismiss();
        ErrorDialog.errorDialog(context, getString(R.string.app_name), error);
    }

    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        if (!isFinishing()) progressDialog.dismiss();
    }
}
