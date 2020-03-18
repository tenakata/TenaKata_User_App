package com.tenakata.Utilities;


import com.tenakata.Application.App;
import com.tenakata.BuildConfig;
import java.util.HashMap;
import java.util.TimeZone;

public class HRUrlFactory {

    private static final String URL_DEV = "http://ec2-34-221-232-228.us-west-2.compute.amazonaws.com/";     // Development
    private static final String URL_PROD = "http://ec2-34-221-232-228.us-west-2.compute.amazonaws.com/";   // Live
    private static final String API_VERSION = "";

    public static boolean isModeDevelopment() { return /*BuildConfig.DEBUG*/true; }

    public static String getBaseUrl(){

        if(isModeDevelopment()){
            return  URL_DEV;
        }else {
            return URL_PROD;
        }

        //return isModeDevelopment() ? URL_DEV : URL_PROD;
    }

    public static String getBaseUrlWithApiVersioning(){
        return getBaseUrl().concat(API_VERSION);
    }

    private static final String kHeaderDevice = "deviceType";
    private static final String kHeaderAccessToken = "token";
    private static final String vHeaderDevice = "android";
    private static final String kTimezone = "Timezone";
    private static final String KDeviceToken = "deviceToken";

    public static String generateUrl(String urlSuffix){
        return getBaseUrl().concat(urlSuffix);
    }

    public static String generateUrlWithVersion(String urlSuffix){
        return getBaseUrl().concat(API_VERSION).concat(urlSuffix);
    }

    public static HashMap<String, String> getDefaultHeaders(){
        HashMap<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("x-api-key", "admin@123");
        defaultHeaders.put(kHeaderDevice, vHeaderDevice);
        defaultHeaders.put(kTimezone, getTimezone());
        defaultHeaders.put("currentVersion", BuildConfig.VERSION_NAME);
       // defaultHeaders.put(KDeviceToken, FirebaseInstanceId.getInstance().getToken());
        return defaultHeaders;
    }

    public static HashMap<String, String> getAppHeaders(){
        HashMap<String, String> appHeaders = getDefaultHeaders();
        appHeaders.put(kHeaderAccessToken, App.getInstance().getAccessToken());
        return appHeaders;
    }

    public static String getTimezone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID() + "";
    }

    /*public static String getDeviceToken(){
        return  FirebaseInstanceId.getInstance().getToken();
    }*/


}
