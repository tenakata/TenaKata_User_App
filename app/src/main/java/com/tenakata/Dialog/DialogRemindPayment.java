package com.tenakata.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tenakata.R;
import com.tenakata.Utilities.HRValidationHelper;

public class DialogRemindPayment implements View.OnClickListener {

    private Context context;
    private Dialog dialog;
    private final String name;
    private final String phone;
    private final String date;
    private ReminderCallBack callBack;
    private String totalAmount;
    private String id;
    private  String defaultMessage;

    public DialogRemindPayment(Context context,String totalAmount,String name, String phone, String date,ReminderCallBack callBack,String Id) {
        this.context = context;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.callBack = callBack;
        this.totalAmount = totalAmount;
        this.id = Id;
        showImage();
    }

    private void showImage() {
        dialog = new Dialog(context);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_remind);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (screenWidth - (screenWidth * 20 / 100));//To make the dialog fill up_to 90% of the screenwidth.
        lp.height = (screenHeight - (screenHeight * 30 / 100));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        TextView btnCancel = dialog.findViewById(R.id.viewBtnCancel);
        TextView amountTv = dialog.findViewById(R.id.textView5);
        final TextView message = dialog.findViewById(R.id.editText3);
       // defaultMessage="This is to remind you that you owe " +name+"\n"+phone+"\namount: "+totalAmount+" for purchases done on "+date+".\nPlease pay up. \nThank you.\nPowered by Tenakata. For Help Call 0728888863!";
        message.setHint(String.valueOf("This is to remind you that you owe " +name+"\n"+phone+"\namount: "+totalAmount+" for purchases done on "+date+".\nPlease pay up. \nThank you.\nPowered by Tenakata. \nFor Help Call 0728888863!"));
        amountTv.setText(HRValidationHelper.optional(totalAmount));
        TextView viewBtnRemind = dialog.findViewById(R.id.viewBtnRemind);
        View view = dialog.findViewById(R.id.view2);

        viewBtnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (callBack!=null){
                        if(message.getText().toString().length()==0){
                            callBack.onRemind(id,message.getHint().toString());
                            close();
                        }else {
                            callBack.onRemind(id,message.getText().toString());
                            close();
                        }

                    }
            }
        });


        btnCancel.setOnClickListener(this);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    private void close() {
        try {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewBtnCancel:
                close();
                break;
        }
    }

    public interface ReminderCallBack{
        void onRemind(String Id,String message);
    }
}