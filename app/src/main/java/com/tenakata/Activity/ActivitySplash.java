package com.tenakata.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.tenakata.Utilities.DimentionSessionManager;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.R;
import com.tenakata.databinding.ActivitySplashBinding;

public class ActivitySplash extends AppCompatActivity {
    private Context context;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context =this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (HRPrefManager.getInstance(context).getKeyIsLoggedIn()){
                    startActivity(IntentHelper.getDashboard(context));
                }else if (!HRPrefManager.getInstance(context).getKeyISStart()){
                    startActivity(IntentHelper.getTutorials(context));
                }else {
                    startActivity(IntentHelper.getBioMetric(context));
                }
                finish();
            }
        }, 2000);
        deviceDimention();
    }

    private void deviceDimention() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        DimentionSessionManager dimentionSessionManager = new DimentionSessionManager(this);

        if (!dimentionSessionManager.isDimentionAvailable()) {

            dimentionSessionManager.saveDeviceDimention(displaymetrics.widthPixels, displaymetrics.heightPixels);
        }
    }
}
