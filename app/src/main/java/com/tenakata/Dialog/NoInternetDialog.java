package com.tenakata.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tenakata.R;


public class NoInternetDialog {

    public static void show(final Context context){
        Activity activity = (Activity)context;
        if(activity==null || activity.isFinishing())return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
       /* LayoutInflater myLayout = LayoutInflater.from(App.getInstance());
        View dialogView = myLayout.inflate(R.layout.alerts_two_buttons, null);
        builder.setView(dialogView);*/
        builder.setTitle(R.string.no_internet);
        builder.setMessage(R.string.no_network_connection);

        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(context instanceof Events){
                    Events events = (Events)context;
                    events.onInternetDialogButtonClicked();
                }
            }
        });
        builder.show();
     }

    public static void show(final Context context, final boolean canDismiss, final boolean shouldFinish){
        Activity activity = (Activity)context;
        if(activity==null || activity.isFinishing())return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
       /* LayoutInflater myLayout = LayoutInflater.from(App.getInstance());
        View dialogView = myLayout.inflate(R.layout.alerts_two_buttons, null);
        builder.setView(dialogView);*/
        builder.setTitle(R.string.no_internet);
        builder.setMessage(R.string.no_network_connection);

        if(!canDismiss){
            builder.setCancelable(false);
        }

        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(shouldFinish){
                    Activity callingActivity = (Activity)context;
                    callingActivity.finish();
                }
                if(context instanceof Events){
                    Events events = (Events)context;
                    events.onInternetDialogButtonClicked();
                }
            }
        });
        builder.show();
    }

    public interface Events{

        void onInternetDialogButtonClicked();
    }
}
