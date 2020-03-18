package com.tenakata.Network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.tenakata.Application.App;
import com.tenakata.BuildConfig;
import com.tenakata.CallBacks.AuthenticationCallBacks;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRNetworkUtils;
import com.tenakata.Utilities.HRUrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Authentication {

    public static void post(@NonNull final String url, @NonNull final HashMap<String, String> params,
                            @NonNull final BaseCallBacks callBacks, final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getAppHeaders();
            } else {
                headers = HRUrlFactory.getDefaultHeaders();
            }
            hit(full_url, params, headers, Request.Method.POST, callBacks);

        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void get(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                           final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void getWithoutLoader(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                                        final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            //callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void getWithAppVersion(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                                         final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void postWithAppVersion(@NonNull final String url, @NonNull final HashMap<String, String> params, @NonNull final BaseCallBacks callBacks,
                                          final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            hit(full_url, params, headers, Request.Method.POST, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void print(String url, String response, String params, String headers) {
        if (BuildConfig.DEBUG) {
            if (null== response){
                Log.i("null response","null response");
            }else {
                Log.i(url, response.concat("\n\n").concat("params: ").concat(params)
                        .concat("\n\n").concat("headers: ").concat(headers).concat("\n\n"));
            }

        }
    }

    public static void hit(@NonNull final String url, final HashMap<String, String> params,
                           @NonNull final HashMap<String, String> headers, int method,
                           @NonNull final BaseCallBacks callBacks) {

        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response, params != null ? params.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                return headers;
            }
        };


        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void homePageListingAPi(@NonNull final String url, final HashMap<String, String> params,
                           @NonNull final HashMap<String, String> headers,
                           @NonNull final BaseCallBacks callBacks) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }
        String full_url = HRUrlFactory.generateUrlWithVersion(url);

        StringRequest request = new StringRequest(Request.Method.POST,full_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response, params != null ? params.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                return headers;
            }
        };


        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void updateUserDetails(String driver_id, String f_name, String l_Name, String contact, String imagePath,
                                         @NonNull final BaseCallBacks callBacks) {


        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion("");

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }

            public String getBodyContentType() {
                return "multipart/form-data; boundary=MINE_BOUNDARY";
            }
        };

        request.addStringParam("driver_id", driver_id);
        request.addStringParam("first_name", f_name);
        request.addStringParam("last_name", l_Name);
        request.addStringParam("econtact", contact);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam("image", imagePath);
            } else {
                request.addFile("image", imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void addOdoMeterApi(String odometerReading,
                                      String imagePath,
                                      @NonNull final BaseCallBacks callBacks, final String Url,String tripId) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                Url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(Url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };

        request.addStringParam("preOdometerReading", odometerReading);
        request.addStringParam("tripId", tripId);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam("preOdometerImage", imagePath);
            } else {
                request.addFile("preOdometerImage", imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void updateDateOFBirth(Context context, final AuthenticationCallBacks callBacks,
                                          final String Url,
                                          final JSONObject jsonObject,final HashMap<String, String> headers) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(Url, response.toString(), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(Url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(Url, String.valueOf(""), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        Volley.newRequestQueue(context).add(request);

    }

    public static void checkUsers(final Context context, final String url,
                                 final AuthenticationCallBacks callBacks,
                                 final HashMap<String, String> paramsession,final HashMap<String,String> headers) {

        final JSONObject loginParams = new JSONObject(paramsession);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, loginParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), paramsession.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactNotExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, "", paramsession.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            public Map<String, String> getHeaders() {
                return headers;
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void apiOfLogin(Context context, final String url,
                                  final AuthenticationCallBacks callBacks, final JSONObject jsonObject,
                                  final HashMap<String, String> headers) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, "", jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public static void objectRequestAccount(Context context, final String url,
                                            final BaseCallBacks callBacks, final JSONObject jsonObject,
                                            final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseDLNotExit
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice
                                || resObj.getInt(HRAppConstants.kResponseCode) == 425) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequestLandingPageApi(Context context, final String url,
                                            final BaseCallBacks callBacks, final JSONObject jsonObject,
                                            final HashMap<String, String> headers,boolean isShowLoader) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isShowLoader) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseDLNotExit
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice
                                || resObj.getInt(HRAppConstants.kResponseCode) == 425) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void sendMessage(Context context, final String url,
                                   final BaseCallBacks callBacks, final JSONObject jsonObject,
                                   final HashMap<String, String> headers, boolean isLoaderEnable) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isLoaderEnable) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequestWithoutLoader(Context context, final String url,
                                                  final BaseCallBacks callBacks, final JSONObject jsonObject,
                                                  final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequest(Context context, final String url,
                                     final BaseCallBacks callBacks, final JSONObject jsonObject,
                                     final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }


    public static void searchFilterApi(final Context context, final String url,
                                       final BaseCallBacks callBacks, final JSONObject jsonObject,
                                       final HashMap<String, String> headers, boolean isLaderEnable) {
        if (HRUrlFactory.isModeDevelopment()) {
            print(url, "Params:" + jsonObject, "", "");
        }
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isLaderEnable && !((Activity) context).isFinishing()) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                String page = "";
                                page = jsonObject.getString("page");
                                if (page != null) {
                                    if (page.equals("1")) {
                                        if (r instanceof Boolean) {
                                            callBacks.onTaskSuccess((boolean) r);
                                        } else {
                                            callBacks.onTaskSuccess(r);

                                        }
                                    } else {
                                        if (r instanceof Boolean) {
                                            callBacks.onLoadMore((boolean) r);
                                        } else {
                                            callBacks.onLoadMore(r);
                                        }
                                    }
                                }

                            } else {
                                callBacks.onTaskError(null);
                            }

                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void simpleRequest(final Context context, final String url,
                                     final BaseCallBacks callBacks,
                                     final HashMap<String,String> params) {
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                print(url, String.valueOf(response), params.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskSuccess("");
                        }
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, "", params.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HRUrlFactory.getAppHeaders();
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                return params;
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void multiPartRequest(String user_id, String amount, String item_list,
                                        String payment_type, String sales_purchases,String date,String attach_recepit,String Url,
                                        @NonNull final BaseCallBacks callBacks,String cash_or_credit,String phone,String id_no,String name) {


        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion(Url);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };




        request.addStringParam("user_id", user_id);
        request.addStringParam("amount", amount);
        request.addStringParam("item_list", item_list);
        request.addStringParam("payment_type", payment_type);
        request.addStringParam("sales_purchases", sales_purchases);
        request.addStringParam("date", date);

        if (cash_or_credit.equalsIgnoreCase("credit")){
            request.addStringParam("phone", phone);
            request.addStringParam("id_no", id_no);
            request.addStringParam("name", name);
        }

        if (attach_recepit != null) {
            if (android.util.Patterns.WEB_URL.matcher(attach_recepit).matches()) {
                request.addStringParam("attach_recepit", attach_recepit);
            } else {
                request.addFile("attach_recepit", attach_recepit);
            }
            Log.i("Path", attach_recepit);
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }


    public static void objectApi(Context context, final String url,
                                 final BaseCallBacks callBacks, final JSONObject jsonObject,
                                 final HashMap<String, String> headers, boolean isLaderEnable) {
        if (isLaderEnable) callBacks.showLoader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));

                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void carRegistrationPhotoReq(String kID, String kImage, String id,
                                               String imagePath, String kImageType, String
                                                       imageType, String Url,
                                               @NonNull final BaseCallBacks callBacks) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion(Url);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };
        request.addStringParam(kID, id);
        request.addStringParam(kImageType, imageType);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam(kImage, imagePath);
            } else {
                request.addFile(kImage, imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void postTypeWithLoader(@NonNull final String url,
                                          @NonNull final HashMap<String, String> headers, final HashMap<String, String> params, int method,
                                          @NonNull final BaseCallBacks callBacks) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response.toString(), "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void object(final Context context, final String url,
                              final BaseCallBacks callBacks,
                              final JSONObject jsonObject) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskSuccess("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, error.getMessage(), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }


}
