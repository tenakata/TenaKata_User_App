package com.tenakata.fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.core.app.ActivityCompat;

/**
 * author @Divyanshu Tayal
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//
    private Context context;
    private FingerprintCallBacks callBacks;

    FingerprintHandler(Context mContext) {
        context = mContext;
    }

    void setCallBacks(FingerprintCallBacks callBacks){
        this.callBacks = callBacks;
    }

    void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        //super.onAuthenticationError(errorCode, errString);
        if(callBacks!=null)callBacks.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        //super.onAuthenticationHelp(helpCode, helpString);
        if(callBacks!=null)callBacks.onAuthenticationHelp(helpCode, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        //super.onAuthenticationSucceeded(result);
        if(callBacks!=null)callBacks.onAuthenticationSucceeded(result);
    }

    @Override
    public void onAuthenticationFailed() {
        //super.onAuthenticationFailed();
        if(callBacks!=null)callBacks.onAuthenticationFailed();
    }
}