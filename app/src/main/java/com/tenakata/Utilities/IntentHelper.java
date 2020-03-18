package com.tenakata.Utilities;

import android.content.Context;
import android.content.Intent;

import com.tenakata.Activity.ActivityBioMetricLogin;
import com.tenakata.Activity.ActivityForgotPassword;
import com.tenakata.Activity.ActivityLogin;
import com.tenakata.Activity.ActivityLoginWithMpin;
import com.tenakata.Activity.ActivityOTPVerification;
import com.tenakata.Activity.ActivityTutorials;
import com.tenakata.Activity.ActivityDashboard;
import com.tenakata.Activity.ActivityVerifyMobileNumber;
import com.tenakata.BuildConfig;
import com.tenakata.R;

public class IntentHelper {

    public static Intent getTutorials(Context context) {
        return new Intent(context, ActivityTutorials.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getDashboard(Context context) {
        return new Intent(context, ActivityDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getLogin(Context context) {
        return new Intent(context, ActivityLogin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getBioMetric(Context context) {
        return new Intent(context, ActivityBioMetricLogin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getVerifyMobileNumber(Context context) {
        return new Intent(context, ActivityVerifyMobileNumber.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getOtpVerification(Context context) {
        return new Intent(context, ActivityOTPVerification.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getForgotPassword(Context context) {
        return new Intent(context, ActivityForgotPassword.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getActivityLoginWithMpin(Context context) {
        return new Intent(context, ActivityLoginWithMpin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }






    public static void shareAPP(Context context, String description) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String shareMessage = description+"\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Share Via"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
