package com.tenakata.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.tenakata.CallBacks.GPSCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahman on 6/26/2018.
 */

public class GPSBroadCastReceiver extends BroadcastReceiver {
    protected List<GPSCallBack> gpsCallBacks;
    protected Boolean connected;

    public GPSBroadCastReceiver() {
        gpsCallBacks = new ArrayList<>();
        connected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent ) {


        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            connected = true;
        } else {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(GPSCallBack callBack : gpsCallBacks)
            notifyState(callBack);
    }

    private void notifyState(GPSCallBack callBack) {
        if(connected == null || callBack == null)
            return;
        if(connected)
            callBack.onGPSFound();
        else
            callBack.onGPSnotFound();
    }

    public void addListener(GPSCallBack callBack) {
        gpsCallBacks.add(callBack);
        notifyState(callBack);
    }

    public void removeListener() {
        gpsCallBacks=null;
    }

}
