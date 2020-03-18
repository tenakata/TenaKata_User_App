package com.tenakata.Dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tenakata.R;

public class GPSErrorDialog {

    private Dialog errorDialog;
    private Context context;
    private int num = 1;
    private Events dialogCallBacks;
    private boolean isDialogShowing = false;
    //private Fonts fonts;

    public GPSErrorDialog(Context context, Events dialogCallBacks) {
        this.context = context;
        this.dialogCallBacks = dialogCallBacks;
    }

    public void show(){

        errorDialog = new Dialog(context);

        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        errorDialog.setContentView(R.layout.activity_connectivity_receiver);

        errorDialog.setCancelable(false);

        errorDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(num>=3){
                        dialogCallBacks.onGPSDialogSayFinish();
                    }else {
                        if(num==1){
                            dialogCallBacks.onGPSDialogSendsMessage(context.getResources().getString(R.string.message_back_pressed_text));
                        }
                        num = num+1;
                    }
                }
                return true;
            }
        });

        if(errorDialog.getWindow()!=null){

            errorDialog.getWindow().setBackgroundDrawable(null);

            errorDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            errorDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        }

        TextView errorText = errorDialog.findViewById(R.id.errorText);
        TextView textView47 = errorDialog.findViewById(R.id.textView47);
        TextView tapretry1 = errorDialog.findViewById(R.id.tapretry1);
        Button retryButton = errorDialog.findViewById(R.id.retry);


        errorText.setText(Html.fromHtml(context.getResources().getString(R.string.unable_to_get_gps)));
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForGPS();
            }
        });

        errorDialog.show();
        isDialogShowing = true;
    }

    private void checkForGPS(){
        if(isGPSEnabled()){
            errorDialog.dismiss();
        }else {
            Toast.makeText(context,context.getResources().getString(R.string.no_gps) , Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGPSEnabled(){
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }

    public boolean isDialogShowing(){
        return isDialogShowing;
    }

    public void dismiss(){
        isDialogShowing = false;
        errorDialog.dismiss();
    }

    public interface Events{

        void onGPSDialogSayFinish();

        void onGPSDialogSendsMessage(String message);
    }

}
