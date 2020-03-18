package com.tenakata.CallBacks;

import java.util.ArrayList;


public interface BaseCallBacks {

    void onTaskSuccess(Object responseObj);

    void onTaskSuccess(ArrayList<?> list);

    void onTaskSuccess(boolean success);

    void onTaskError(String errorMsg);

    void onSessionExpire(String message);

    void onAppNeedUpdate(String message);
    
    void onAppNeedLogin();

    void onAppNeedForceUpdate(String message);

    void onInternetNotFound();

    void delayAppUpdate();

    void showLoader();

    void dismissLoader();

    void showFullScreenLoader();

    void onFragmentDetach(String fragmentTag);

    void onLoadMore(Object responseObj);

    void onLoadMore(ArrayList<?> list);

}
