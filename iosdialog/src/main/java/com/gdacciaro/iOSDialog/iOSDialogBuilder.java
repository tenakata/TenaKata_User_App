package com.gdacciaro.iOSDialog;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by rahman
 * on 17/03/18.
 */

public class iOSDialogBuilder {
    private Typeface tf,tf1;
    private boolean bold,cancelable;
    private String title, subtitle, okLabel, koLabel;
    private Context context;
    private iOSDialogClickListener positiveListener;
    private iOSDialogClickListener negativeListener;

    public iOSDialogBuilder(Context context) {
        this.context = context;
    }

    public iOSDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public iOSDialogBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public iOSDialogBuilder setBoldPositiveLabel(boolean bold) {
        this.bold = bold;
        return this;
    }

    public iOSDialogBuilder setFont(Typeface font) {
        this.tf=font;
        return this;
    }
    public iOSDialogBuilder setTitleFont(Typeface font){
        this.tf1=font;
        return this;
    }

    public iOSDialogBuilder setCancelable(boolean cancelable){
        this.cancelable=cancelable;
        return this;
    }

    public iOSDialogBuilder setNegativeListener(String koLabel,iOSDialogClickListener listener) {
        this.negativeListener=listener;
        this.koLabel=koLabel;
        return this;
    }

    public iOSDialogBuilder setPositiveListener(String okLabel,iOSDialogClickListener listener) {
        this.positiveListener = listener;
        this.okLabel=okLabel;
        return this;
    }

    public iOSDialog build(){
        iOSDialog dialog = new iOSDialog(context,title,subtitle, bold, tf,cancelable,tf1);
        dialog.setNegative(koLabel,negativeListener);
        dialog.setPositive(okLabel,positiveListener);
        return dialog;
    }

}
