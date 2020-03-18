package com.tenakata.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author Hafijur
 */
public class DimensionHelper {

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private int convertDpToPx(Context context,int dp){
        return Math.round(dp*(context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));

    }

    private int convertPxToDp(int px){
        return Math.round(px/(Resources.getSystem().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    private static float dpFromPx(Context context, float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(Context context, float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int getDeviceWidthInDP(Context context){
        return (int)dpFromPx(context, context.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getDrawerDragDistance(Context context){
        int totalWidthInDp = (int)dpFromPx(context, context.getResources().getDisplayMetrics().widthPixels);
        double widthForContentView = totalWidthInDp*0.35;
        return totalWidthInDp-(int)widthForContentView;
    }

}
