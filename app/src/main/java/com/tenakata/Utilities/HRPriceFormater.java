package com.tenakata.Utilities;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HRPriceFormater {

    public static String roundDecimalByTwoDigits(Object input) {
        if (input==null){
            return "";
        }

        try {
           // DecimalFormat df = new DecimalFormat("#0.00");
           // return df.format(input).replace(",", ".");
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat)nf;
            formatter.applyPattern("#0.00");
            return formatter.format(input).replace(",", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String roundDecimalByOneDigit(Object input) {
        try {
            DecimalFormat df = new DecimalFormat("#0.0");
            return df.format(input).replace(",", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getAmountInCents(double amount) {
        return Math.round((amount * 100));
    }

    public static double getAmountInDollars(int amountInCents) {
        return amountInCents / 100;
    }

    public static double getAmountInDollars(double amountInCents) {
        return amountInCents / 100;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String changeDateToTime(String serverdate) {
        if (serverdate==null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date d = null;
        try {
            d = sdf.parse(serverdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.format(d);
    }

    public static String graphWeekFormater(String date) {
        if (date==null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("EEEE", Locale.US);

        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(d);
    }

    public static String simplegraphWeekFormater(String date) {
        if (date==null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("EEEE", Locale.US);

        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(d);
    }

    public static String changeMonthFormate(String serverdate) {
        if (serverdate==null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", new Locale("en"));
        SimpleDateFormat output = new SimpleDateFormat("MMM", new Locale("en"));
        Date d = null;
        try {
            d = sdf.parse(serverdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(d);
    }

    public static String time(String time) {
        Date dateObj = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy",new Locale("en"));
            dateObj = sdf.parse(time);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MMM dd, yyyy",new Locale("en")).format(dateObj);
    }

    public static String formatMonthAndYear(String time) {
        if (time==null){
            return "";
        }
        Date dateObj = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy",new Locale("en"));
            dateObj = sdf.parse(time);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MMMM dd, yyyy",new Locale("en")).format(dateObj);
    }

    public static int getNumberOFDaysBetweenTwoDates(String startDate, String endDate) {
        float daysBetween = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd",new Locale("en"));
        try {
            Date dateBefore = myFormat.parse(startDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) daysBetween;
    }

    public static String dateFormat(String time, String inputFormat, String outputFormat) {
        Date dateObj = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(inputFormat,new Locale("en"));
            dateObj = sdf.parse(time);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(outputFormat,new Locale("en")).format(dateObj);
    }

    public static int getDrawerDragDistance(Context context){
        int totalWidthInDp = (int)dpFromPx(context, context.getResources().getDisplayMetrics().widthPixels);
        double widthForContentView = totalWidthInDp*0.35;
        return totalWidthInDp-(int)widthForContentView;
    }

    private static float dpFromPx(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    private static int getModolus(int min){
        int mod = (min + 30)%60;
        return (min + 30) - mod;
    }

    public static String getTime(String time){

        String parts[] = time.split(":");
        int min = Integer.parseInt(parts[1]);
        int hours = Integer.parseInt(parts[0]);

        if (getModolus(min) == 0){
            int h = hours + 1;
            int m = 30;
            return h+":"+m;
        }else if (getModolus(min) == 60){
            int h = hours + 2;
            int m = 0;
            return h+":00";
        }else {
            return "00:00";
        }
    }

    public static int startCalValidation(String time){
        String parts[] = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int min = Integer.parseInt(parts[1]);
        if (hours>22){
            return 0;
        }else {
            return 0;
        }

    }

}
