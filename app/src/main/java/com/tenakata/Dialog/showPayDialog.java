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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tenakata.R;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRValidationHelper;


public class showPayDialog implements View.OnClickListener {

    private Context context;
    private Dialog dialog;
    private String amount;
    private String id;
    private String paymentMode = "M-pesa";
    private onAmountPaid callBakc;

    public showPayDialog(Context context,String id,
                         String totalAmount,onAmountPaid callBakc) {
        this.context = context;
        this.id = id;
        this.amount = totalAmount;
        this.callBakc = callBakc;
        showImage();
    }

    private void showImage() {
        dialog = new Dialog(context);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (screenWidth - (screenWidth * 20/ 100));
        lp.height = (screenHeight - (screenHeight * 30 / 100));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);

        TextView btnCancel = dialog.findViewById(R.id.viewBtnCancel);
        TextView totalAmount = dialog.findViewById(R.id.textView5);
        totalAmount.setText(HRValidationHelper.optional(HRPriceFormater.roundDecimalByTwoDigits(Double.parseDouble(amount))));

        final TextView enterAmount = dialog.findViewById(R.id.viewAmount);
        final TextView viewNarration = dialog.findViewById(R.id.viewNarration);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                paymentMode = radioButton.getText().toString();

            }
        });


        TextView btnPay = dialog.findViewById(R.id.viewBtnPay);
        View view = dialog.findViewById(R.id.view2);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation(enterAmount.getText().toString(),
                        paymentMode,viewNarration.getText().toString(),Double.parseDouble(amount))){
                    callBakc.onAmountPaidByUser(id,enterAmount.getText().toString(),
                            "credit",viewNarration.getText().toString());
                    close();

                }
            }
        });
        btnCancel.setOnClickListener(this);

        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    private boolean validation(String amount,String mode,String naration,double totalAmountTransaction){
        if (HRValidationHelper.isNull(amount)){
            Toast.makeText(context,"Enter Amount",Toast.LENGTH_SHORT).show();
            return false;
        }else if (HRValidationHelper.isNull(mode)){
            Toast.makeText(context,"Select payment mode",Toast.LENGTH_SHORT).show();
            return false;
        }else if(HRValidationHelper.isNull(naration)){
            Toast.makeText(context,"Enter Narration",Toast.LENGTH_SHORT).show();
                return false;
        }else if (Double.parseDouble(amount)>totalAmountTransaction){
            Toast.makeText(context,"Enter Amount can not be greater than Total Amount",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        switch (view.getId()){
            case R.id.viewBtnCancel:
                close();
                break;
        }
    }

    public interface onAmountPaid{
        void onAmountPaidByUser(String id,String amount,String mode,String narration);
    }
}
