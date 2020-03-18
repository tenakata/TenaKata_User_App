package com.tenakata.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class BioMetricLoginSession {

    private SharedPreferences bioMetric;
    private SharedPreferences.Editor editor;

    private final String sp_name="biometricLogin";

    public static final String KEY_PHONE="KEY_PHONE";
    public static final String KEY_COUNTRY_CODE="KEY_COUNTRY_CODE";
    public static final String KEY_PASSWORD="KEY_PASSWORD";
    public static final String KEY_ROLE="KEY_ROLE";



    public BioMetricLoginSession(Context context){

        bioMetric=context.getSharedPreferences(sp_name,0);
    }

    public void saveDetails(String phone,String code,String password,String role){

        editor=bioMetric.edit();
        editor.putString(KEY_PHONE,phone);
        editor.putString(KEY_COUNTRY_CODE,code);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_ROLE,role);
        editor.apply();
    }

    public String getString(String value){
        return bioMetric.getString(value,"");
    }
}
