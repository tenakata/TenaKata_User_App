package com.tenakata.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.tenakata.R;
import com.tenakata.Utilities.FontFamily;

public class ErrorDialog {

    public static void forceUpdateDialog(final Context context, String setTitle, String setSubTitle) {

        new iOSDialogBuilder(context)
                .setTitle(setTitle)
                .setSubtitle(setSubTitle)
                .setBoldPositiveLabel(false)
                .setFont(FontFamily.getPtRegular())
                .setTitleFont(FontFamily.getMonBold())
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        final String appPackageName = context.getPackageName();
                        if (appPackageName != null) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    }
                })
                .build().show();
    }

    public static void normalUpdateDialog(final Context context, String setTitle, String setSubTitle) {

        new iOSDialogBuilder(context)
                .setTitle(setTitle)
                .setSubtitle(setSubTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setFont(FontFamily.getPtRegular())
                .setTitleFont(FontFamily.getMonBold())
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        final String appPackageName = context.getPackageName();
                        if (appPackageName != null) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    }
                })
                .setNegativeListener(context.getResources().getString(R.string.cancel_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    public static void errorDialog(final Context context, String setTitle, String setSubTitle) {
        new iOSDialogBuilder(context)
                .setTitle(setTitle)
                .setSubtitle(setSubTitle)
                .setBoldPositiveLabel(false)
                .setFont(FontFamily.getPtRegular())
                .setTitleFont(FontFamily.getMonBold())
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    public static void show(final Context context, String setSubTitle) {

        String message;
        if (setSubTitle == null) {
            message= context.getString(R.string.error_sub_title_text);
        } else {
            message = setSubTitle;
        }

        new iOSDialogBuilder(context)
                .setTitle(context.getString(R.string.app_name))
                .setSubtitle(message)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    public static AlertDialog dialog(Context context, String message) {
        Activity activity = (Activity) context;
        if (activity == null || activity.isFinishing()) return null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.error_title_text));
        if (message == null) {
            builder.setMessage(context.getString(R.string.error_sub_title_text));
        } else {
            builder.setMessage(message);
        }

        builder.setPositiveButton(context.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static SpannableString typeface(Typeface typeface, CharSequence string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }



    public static Dialog createDialog(Context context, int dialogLayout) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLayout);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setAttributes(layoutParams);
        try {
            dialog.show();
        }
        catch (Exception e)
        {}
        return dialog;
    }

}
