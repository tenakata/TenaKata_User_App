package com.tenakata.Utilities;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import com.tenakata.Application.App;

public class HRNetworkUtils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager!=null && connectivityManager.getActiveNetworkInfo()!=null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
