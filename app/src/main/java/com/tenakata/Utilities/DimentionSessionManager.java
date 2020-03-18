package com.tenakata.Utilities;

import android.content.Context;
import android.content.SharedPreferences;


public class DimentionSessionManager {

    private SharedPreferences dimSP;
    private SharedPreferences.Editor editor;

    private final String sp_name="dimen_sp_tak";

    private static final String tkIsDimentionAlreadySaved="is_dimention_present";
    private static final String kDevice_width="device_width";
    private static final String kDevice_Height="device_height";

    private static final String coordinatorallSummaryrow_width="row recycleview_width";

    public DimentionSessionManager(Context context){

        dimSP=context.getSharedPreferences(sp_name,0);
    }

    public void saveDeviceDimention(int widthPixel,int heightPixel){

        editor=dimSP.edit();
        editor.putInt(kDevice_width,widthPixel);
        editor.putInt(kDevice_Height,heightPixel);
        editor.putInt(coordinatorallSummaryrow_width,(int)(widthPixel*0.75));
        editor.apply();
    }

    public int getWidth(){

        return dimSP.getInt(coordinatorallSummaryrow_width,0);
    }

    public boolean isDimentionAvailable(){
        return  dimSP.getBoolean(tkIsDimentionAlreadySaved,false);
    }
}
