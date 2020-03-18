package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.LoginModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRLogger;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityLoginWithMpinBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityLoginWithMpin extends AppCompatActivity implements View.OnClickListener, AuthenticationCallBacks {
    private Context context;
    private ActivityLoginWithMpinBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_with_mpin);
        context = this;
        binding.viewShowHide.setOnClickListener(this);
        progressDialog = new ProgressDialog(context);

        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length()==4){
                    apiLoginWithmPin(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewShowHide:
                if(binding.viewShowHide.getText().toString().equals("Show")){
                    binding.firstPinView.setTransformationMethod(new PasswordTransformationMethod());
                    binding.viewShowHide.setText("Hide");
                } else{
                    binding.firstPinView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.viewShowHide.setText("Show");
                }
                break;
        }
    }

    @Override
    public void onSuccessCallback(Object response) {
        if (!isFinishing()) progressDialog.dismiss();
        if (response instanceof LoginModel) {

            HRPrefManager.getInstance(context).setUserDetail((LoginModel) response);
            HRPrefManager.getInstance(context).setKeyIsLoggedIn(true);
            startActivity(IntentHelper.getDashboard(context));
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

    private void apiLoginWithmPin(String pin){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("pin", pin);
            jsonObject.put("role", "user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.apiOfLogin(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_LOGIN_WITH_MPIN),
                this, jsonObject, HRUrlFactory.getDefaultHeaders());
    }
}
