package com.tenakata.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.fonts.FontFamily;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.tenakata.Application.App;
import com.tenakata.BroadcastReceiver.GPSBroadCastReceiver;
import com.tenakata.BroadcastReceiver.NetworkStateReceiver;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.CallBacks.GPSCallBack;
import com.tenakata.CallBacks.LogoutCallBacks;
import com.tenakata.CallBacks.NetworkStateReceiverListener;
import com.tenakata.Dialog.ErrorDialog;
import com.tenakata.Dialog.GPSErrorDialog;
import com.tenakata.Dialog.InternetErrorDialog;
import com.tenakata.Dialog.NoInternetDialog;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.IntentHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, BaseCallBacks, LocationListener, GPSCallBack, NetworkStateReceiverListener,
        InternetErrorDialog.Events, GPSErrorDialog.Events, LogoutCallBacks {

    private InternetErrorDialog internetErrorDialog;
    private GPSErrorDialog gpsErrorDialog;
    boolean isGPSAvailable = false;
    boolean isInternetAvailable = false;

    private ProgressDialog progressDialog;
    private static List<AlertDialog> sessionExpireDialogList = new ArrayList<>();
    private static List<AlertDialog> errorDialogList = new ArrayList<>();
    private NetworkStateReceiver networkStateReceiver;
    private GPSBroadCastReceiver gpsBroadCastReceiver;

    private Location mLastLocation;
    private boolean isInBAckground = false;
    private Context context;

    public abstract void onClick(int viewId, View view);




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        context = this;

        internetErrorDialog = new InternetErrorDialog(this, this);
        gpsErrorDialog = new GPSErrorDialog(this, this);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPSnotFound();
        } else {
            onGPSFound();
        }
    }

    public void registerActivityForInternet() {
        networkStateReceiver = new NetworkStateReceiver();
        if (this instanceof NetworkStateReceiverListener) {
            networkStateReceiver.addListener((NetworkStateReceiverListener) this);
        }
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void registerActivityForGPS() {
        gpsBroadCastReceiver = new GPSBroadCastReceiver();
        if (this instanceof GPSCallBack) {
            gpsBroadCastReceiver.addListener((GPSCallBack) this);
        }
        this.registerReceiver(gpsBroadCastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }





    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void blockTouch() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void unBlockTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // case R.id.toolbar_back: onBackPressed(); break;
            default:
                onClick(v.getId(), v);
                break;
        }
    }

    @Override
    public void showLoader() {
        try {
            if (!isFinishing() && !progressDialog.isShowing()) progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showFullScreenLoader() {
        if (!isFinishing()) progressDialog.showDialog(ProgressDialog.DIALOG_FULLSCREEN);
    }

    @Override
    public void dismissLoader() {
        if (!isFinishing() && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        dismissLoader();
        try {
            if (sessionExpireDialogList.size() > 0) {
                for (AlertDialog dialog : sessionExpireDialogList) {
                    if (!isFinishing()) dialog.dismiss();
                }
                sessionExpireDialogList.clear();
            }
            AlertDialog.Builder sessionExpireDialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            sessionExpireDialog.setCancelable(false);
            sessionExpireDialog.setTitle(R.string.app_name);
            sessionExpireDialog.setMessage(message);

            sessionExpireDialog.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    apiLogOut();
                }
            });
            if (!isFinishing()) sessionExpireDialogList.add(sessionExpireDialog.show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiLogOut() {
        /*if (progressDialog != null && !progressDialog.isShowing() && !isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
            Authentication.get(HRAppConstants.URL_LOGOUT, this, false);*/
    }

    @Override
    public void onLogOutSuccess() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
        HRPrefManager.getInstance(App.getInstance()).clearPrefs();
        startActivity(IntentHelper.getLogin(BaseActivity.this));
        finishAffinity();
        //FirebaseAuth.getInstance().signOut();

    }

    @Override
    public void onLogOutError(String message) {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
            HRPrefManager.getInstance(App.getInstance()).clearPrefs();
            startActivity(IntentHelper.getLogin(BaseActivity.this));
            finishAffinity();
    }

    @Override
    public void onAppNeedUpdate(String message) {
        dismissLoader();
        ErrorDialog.normalUpdateDialog(context,getString(R.string.txt_update_available),message);
    }

    @Override
    public void onAppNeedForceUpdate(String message) {
        dismissLoader();
        if (!isFinishing())
        ErrorDialog.forceUpdateDialog(context,getString(R.string.txt_update_available),message);
    }

    public void show(final Activity activity){
        if(activity==null || activity.isFinishing())return;

        new iOSDialogBuilder(activity)
                .setTitle(activity.getString(R.string.app_name))
                .setSubtitle(getString(R.string.txt_login_again))
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        HRPrefManager.getInstance(App.getInstance()).clearPrefs();
                        startActivity(IntentHelper.getLogin(BaseActivity.this));
                        finishAffinity();
                    }
                })
                .build().show();

    }

    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) NoInternetDialog.show(this);
    }

    @Override
    public void delayAppUpdate() {
    }

    @Override
    public void onFragmentDetach(String fragmentTag) {
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        dismissLoader();
    }


    @Override
    public void onTaskSuccess(ArrayList<?> list) {
        dismissLoader();
    }

    @Override
    public void onTaskSuccess(boolean success) {
        dismissLoader();
    }

    @Override
    public void onLoadMore(Object responseObj) {
        dismissLoader();
    }

    @Override
    public void onLoadMore(ArrayList<?> list) {
        dismissLoader();
    }

    @Override
    public void onAppNeedLogin() {
        show(BaseActivity.this);
        dismissLoader();
    }

    @Override
    public void onTaskError(String errorMsg) {
        dismissLoader();
        if (errorDialogList.size() > 0) {
            for (AlertDialog dialog : errorDialogList) {
                if (!isFinishing()) dialog.dismiss();
            }
            errorDialogList.clear();
        }
        if (!isFinishing()) errorDialogList.add(ErrorDialog.dialog(this, errorMsg));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkStateReceiver != null) {
            try {
                // networkStateReceiver.removeListener();
                this.unregisterReceiver(networkStateReceiver);
                //networkStateReceiver=null;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if (gpsBroadCastReceiver != null) {
            try {
                this.unregisterReceiver(gpsBroadCastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isInBAckground) {

            this.registerReceiver(networkStateReceiver,
                    new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

            this.registerReceiver(gpsBroadCastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkStateReceiver != null) {
            try {

                this.unregisterReceiver(networkStateReceiver);

                //gpsBroadCastReceiver.removeListener();
                this.unregisterReceiver(gpsBroadCastReceiver);

                //notificationsBroadCast.removeListener();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onGPSFound() {
        isGPSAvailable = true;
        if (gpsErrorDialog != null) {
            if (gpsErrorDialog.isDialogShowing()) {
                gpsErrorDialog.dismiss();
            }
        }
    }

    @Override
    public void onGPSnotFound() {

        isGPSAvailable = false;
        if (gpsErrorDialog != null) {
            if (!gpsErrorDialog.isDialogShowing()) {
                gpsErrorDialog.show();
            }
        }

    }

    @Override
    public void onInternetConnectionEstablished() {

        isInternetAvailable = true;
        if (internetErrorDialog != null) {
            if (isGPSAvailable) {
            } else {
                onGPSnotFound();
            }
            internetErrorDialog.dismiss();
        }

    }

    @Override
    public void onInternetConnectionNotFound() {
        isInternetAvailable = false;
        internetErrorDialog.show();
    }

    @Override
    public void onGPSDialogSayFinish() {
        finish();
    }

    @Override
    public void onGPSDialogSendsMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternetDialogSayFinish() {
        finish();
    }

    @Override
    public void onInternetDialogSendsMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void killNotification() {
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

}
