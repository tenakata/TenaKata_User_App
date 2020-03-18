package com.tenakata.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.tenakata.Base.BaseActivity;
import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Models.LoginModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.BioMetricLoginSession;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRLogger;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.ActivityBioMetricLoginBinding;
import com.tenakata.databinding.ActivityLoginBinding;
import com.tenakata.fingerprint.FingerprintAuthManager;
import com.tenakata.fingerprint.FingerprintCallBacks;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityBioMetricLogin extends BaseActivity implements View.OnClickListener, FingerprintCallBacks , AuthenticationCallBacks {
    private Context context;
    private ActivityBioMetricLoginBinding binding;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bio_metric_login);
        binding.viewLoginWithContact.setOnClickListener(this);
        binding.viewLoginWithMPin.setOnClickListener(this);
        context = this;

        progressDialog =new ProgressDialog(context);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(isFinishing())return;

            startFingerPrintScanAnimation();
            FingerprintAuthManager authManager = new FingerprintAuthManager(context);
            authManager.setCallBacks(this);
            authManager.initialize();

        }
    }

    private void startFingerPrintScanAnimation(){
        Animation scanningAnimation = AnimationUtils.loadAnimation(context, R.anim.fingerprint_scanning);
        binding.viewTouchIDBar.startAnimation(scanningAnimation);
        binding.viewTouchIDBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int viewId, View view) {
        switch (view.getId()){
            case R.id.arrow_imv:
                break;

            case R.id.viewLoginWithContact:
                startActivity(IntentHelper.getLogin(context));
                break;
            case R.id.viewLoginWithMPin:
                startActivity(IntentHelper.getActivityLoginWithMpin(context));
                break;
        }
    }

    @Override
    public void onFingerprintHardwareNotDetected() {
        //if(callBacks!=null)callBacks.onFingerprintHardwareNotDetected();
    }

    @Override
    public void onFingerprintNotConfigured() {
        //Toast.makeText(context, getString(R.string.error_fingerprint_not_saved), Toast.LENGTH_SHORT).show();
        //if(callBacks!=null)callBacks.onFingerprintNotConfigured();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if(errorCode==7){
            Toast.makeText(context, getString(R.string.error_too_many_attempts), Toast.LENGTH_SHORT).show();

        }
       // if(callBacks!=null)callBacks.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationFailed() {
        //dismiss();
        Toast.makeText(context, getString(R.string.error_unsuccessful_authentication_attempt), Toast.LENGTH_SHORT).show();

        //if(callBacks!=null)callBacks.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(context, helpString.toString(), Toast.LENGTH_SHORT).show();

        //if(callBacks!=null)callBacks.onAuthenticationHelp(helpCode, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(final FingerprintManager.AuthenticationResult result) {
        binding.viewTouchIDBar.setVisibility(View.GONE);
        binding.viewTouchIDBar.setBackgroundColor(Color.WHITE);
        Glide.with(context)
                .load(R.drawable.fingerprint_gif)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .signature(new ObjectKey(System.currentTimeMillis())))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isValidate(binding.parent)) {
                                    apiLogin();
                                }
                            }
                        }, (2200));
                        return false;
                    }
                })
                .into(binding.viewFingerprintImage);
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

    private void apiLogin(){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_PHONE));
            jsonObject.put("country_code", new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_COUNTRY_CODE));
            jsonObject.put("password", new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_PASSWORD));
            jsonObject.put("role", new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_ROLE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.apiOfLogin(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_LOGIN),
                this, jsonObject, HRUrlFactory.getDefaultHeaders());
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

    private boolean isValidate(View view) {
        if (HRValidationHelper.isNull(new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_PHONE))) {
            ErrorDialog.errorDialog(context, getString(R.string.app_name), "Very first time you have to login with credentials");
            return false;
        } else if (HRValidationHelper.isNull(new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_PASSWORD))) {
            ErrorDialog.errorDialog(context, getString(R.string.app_name), "Very first time you have to login with credentials");
            return false;
        }else if (!HRValidationHelper.isValidPassword(new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_PASSWORD))) {
            ErrorDialog.errorDialog(context, getString(R.string.app_name), "Very first time you have to login with credentials");
            return false;
        }else if (HRValidationHelper.isNull(new BioMetricLoginSession(context).getString(BioMetricLoginSession.KEY_COUNTRY_CODE))) {
            ErrorDialog.errorDialog(context, getString(R.string.app_name), "Very first time you have to login with credentials");
            return false;
        }
        return true;
    }
}
