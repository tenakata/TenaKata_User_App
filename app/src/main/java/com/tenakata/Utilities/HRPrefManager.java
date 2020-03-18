package com.tenakata.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tenakata.Models.LoginModel;


public class HRPrefManager {

    private static HRPrefManager instance;
    private static final String PREF_NAME = "TenakataPR";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_IS_LOGGED_IN = "is_Login";
    private static final String KEY_IS_START = "is_start_new";
    private static final String KEY_USER_DETAILS = "KEY_USER_DETAILS";

    private Context context;
    int PRIVATE_MODE = 0;

    public HRPrefManager(Context context) {

        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    public static HRPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new HRPrefManager(context);
        }
        return instance;
    }

    public void setKeyIsLoggedIn(boolean value) {
        setBooleanValue(KEY_IS_LOGGED_IN, value);
    }

    public boolean getKeyIsLoggedIn() {
        return getBooleanValue(KEY_IS_LOGGED_IN);
    }

    public void setKeyIsStart(boolean value) {
        setBooleanValue(KEY_IS_START, value);
    }

    public boolean getKeyISStart() {
        return getBooleanValue(KEY_IS_START);
    }


    public void setKeyRememberMe(boolean value) {
        setBooleanValue(KEY_REMEMBER_ME, value);
    }

    public boolean getKeyRememberMe() {
        return getBooleanValue(KEY_REMEMBER_ME);
    }

    public LoginModel getUserDetail() {
        Gson gson = new Gson();
        String json = preferences.getString(KEY_USER_DETAILS, "");
        return gson.fromJson(json, LoginModel.class);
    }

    public void setUserDetail(LoginModel user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER_DETAILS, json);
        editor.commit();
    }



    public boolean getBooleanValue(String key) {
        return this.preferences.getBoolean(key, false);
    }

    public void setBooleanValue(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public String getStringValue(String key) {
        return this.preferences.getString(key, "");
    }

    public void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }


    private int getIntValue(String key) {
        return this.preferences.getInt(key, 0);
    }

    private void setIntValue(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    private long getLongValue(String key) {
        return this.preferences.getLong(key, 0);
    }

    private void setLongValue(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    public void setLatitudeValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public String getLattitudeValue(String key) {
        return this.preferences.getString(key, null);
    }

    public void setLongitudeValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public String getLongitudeValue(String key) {
        return this.preferences.getString(key, null);
    }

    public void clearPrefs() {
        this.editor.clear();
        HRPrefManager.getInstance(context).setKeyIsStart(true);
        this.editor.commit();
    }

    public void removeFromPreference(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

}
