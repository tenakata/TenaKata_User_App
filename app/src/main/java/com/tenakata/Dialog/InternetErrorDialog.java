package com.tenakata.Dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tenakata.R;


/**
 * Created by rahman on 6/26/2018.
 */

public class InternetErrorDialog {

    private Dialog errorDialog;
    private Context context;
    private int num = 1;
    private Events dialogCallBacks;
    //private Fonts fonts;
    private boolean isShowing=false;

    public InternetErrorDialog(Context context, Events dialogCallBacks) {
        this.context = context;
        this.dialogCallBacks = dialogCallBacks;
    }

    public void show(){

        errorDialog = new Dialog(context, R.style.AlertDialogTheme);

        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        errorDialog.setContentView(R.layout.activity_connectivity_receiver);

        errorDialog.setCancelable(false);

        errorDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(num>=3){
                        dialogCallBacks.onInternetDialogSayFinish();
                    }else {
                        if(num==1){
                            dialogCallBacks.onInternetDialogSendsMessage(context.getResources().getString(R.string.message_back_pressed_text));
                        }
                        num = num + 1;
                    }
                }
                return true;
            }
        });

        if(errorDialog.getWindow()!=null){

            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

            errorDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            errorDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        }

        TextView errorText = errorDialog.findViewById(R.id.errorText);
        Button retryButton = errorDialog.findViewById(R.id.retry);

        errorText.setText(Html.fromHtml(context.getResources().getString(R.string.no_internet)));
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForInternet();
            }
        });

        if (!errorDialog.isShowing())
        errorDialog.show();
        isShowing=true;
    }

    private void checkForInternet(){
        if(isNetworkAvailable()){
            if (errorDialog!=null)
            errorDialog.dismiss();
            isShowing=false;
        }else {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show();
        }
    }

    public void dismiss(){
        if(errorDialog!=null)
            errorDialog.dismiss();
        isShowing=true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface Events{
        void onInternetDialogSayFinish();
        void onInternetDialogSendsMessage(String message);
    }
}
