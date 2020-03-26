package com.tenakata.Activity;


import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tenakata.Base.BaseActivity;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.databinding.ActivityMpinRetypeBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMpinRetype extends BaseActivity {
    ActivityMpinRetypeBinding binding;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    public void onClick(int viewId, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mpin_retype);
        context = this;
        progressDialog = new ProgressDialog(context);
        Intent intent = getIntent();
        final String pin = intent.getStringExtra("pin");


        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 4) {
                    String retypedpin = binding.firstPinView.getText().toString();
                    if (retypedpin.equals(pin)) {
                        Toast.makeText(getApplicationContext(), "password matched", Toast.LENGTH_LONG).show();
                        apiSetmPin(retypedpin);
                    } else {
                        Toast.makeText(getApplicationContext(), "password not matched", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    private void apiSetmPin(String pin) {

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("pin", pin);
            jsonObject.put("role", "user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_CREATE_MPIN),
                this, jsonObject);

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
        finish();

        if (!isFinishing()) progressDialog.dismiss();
        if (responseObj instanceof ModelSuccess) {
            Toast.makeText(getApplicationContext(), "password setup completed", Toast.LENGTH_LONG).show();
            finish();
        }



    }

    @Override
    public void onTaskError(String errorMsg) {
        if (!isFinishing()) progressDialog.dismiss();
        ErrorDialog.errorDialog(context, getString(R.string.app_name), errorMsg);
    }
}

