package com.tenakata.Application;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.tenakata.Utilities.HRPrefManager;


public class App extends MultiDexApplication {

    private Context appContext;

    private Gson gson;
    private RequestQueue requestQueue;

    private static App applicationInstance;

    public static App getInstance() {
        return applicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        appContext = this;
        gson = new Gson();
        FirebaseApp.initializeApp(appContext);
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Gson getGson() {
        return gson;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Drawable getAppDrawable(int drawableID){
        return ContextCompat.getDrawable(appContext, drawableID);
    }

    public String getAccessToken(){
        try {
            if (HRPrefManager.getInstance(appContext).getUserDetail()!=null) {
                return HRPrefManager.getInstance(appContext).getUserDetail().getResult().getToken();
            }else {
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int getAppColor(int colorID){
        return ContextCompat.getColor(appContext, colorID);
    }

}
