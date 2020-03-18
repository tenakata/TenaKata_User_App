package com.tenakata.Utilities;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tenakata.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rahman on 18/1/18.
 */

public class HRLogger {

    private static boolean DEBUG = false;

    public static void d(String tag, String arg) {
        if (isEnable()) {
            log(tag, arg);
        }
    }

    public static void d(String logMsg) {
        if (isEnable()) {
            log(getCurrentClassName(), getCurrentMethodName() + "(): " + logMsg);
        }
    }

    public static void dd(String tag, Object source) {
        if (isEnable()) {
            Object o = getJsonObjFromStr(source);
            if (o != null) {
                try {
                    if (o instanceof JSONObject) {
                        format(tag, ((JSONObject) o).toString(2));
                    } else if (o instanceof JSONArray) {
                        format(tag, ((JSONArray) o).toString(2));
                    } else {
                        format(tag, source);
                    }
                } catch (JSONException e) {
                    format(tag, source);
                }
            } else {
                format(tag, source);
            }
        }
    }

    private static void log(String tag, String msg) {
        Log.i(tag, msg);
    }

    private static String getSplitter(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append("-");
        }
        return builder.toString();
    }

    private static void format(String tag, Object source) {
        log(tag, getSplitter(50) + tag + getSplitter(50));
        log(" ", (String) source);
    }

    private static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    private static String getCurrentClassName() {
        String className = Thread.currentThread().getStackTrace()[4].getClassName();
        String[] temp = className.split("[\\.]");
        className = temp[temp.length - 1];
        return className;
    }

    private static Object getJsonObjFromStr(Object test) {
        Object o = null;
        try {
            o = new JSONObject(test.toString());
        } catch (JSONException ex) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    o = new JSONArray(test);
                }
            } catch (JSONException ex1) {
                return null;
            }
        }
        return o;
    }

    private static boolean isEnable() {
        return DEBUG;
    }

    public static void setEnable(boolean flag) {
        HRLogger.DEBUG = flag;
    }

    public static void showToast(Context context, String message){
        if (isEnable() && context!=null){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showSneckbar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void printLog(String title, String content) {
        if (BuildConfig.DEBUG) {
            Log.d(title, content);
        }
    }

    public static void printELog(String title, String content) {
        if (BuildConfig.DEBUG) {
            Log.e(title, content);
        }
    }
}
