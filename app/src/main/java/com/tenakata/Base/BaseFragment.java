package com.tenakata.Base;

import android.content.Context;

import androidx.fragment.app.Fragment;


import com.tenakata.CallBacks.BaseCallBacks;

import java.util.ArrayList;


public class BaseFragment extends Fragment
        implements BaseCallBacks {

    private BaseCallBacks callBacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BaseCallBacks){
            callBacks = (BaseCallBacks)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBacks = null;
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        if(callBacks!=null) {
            callBacks.onTaskSuccess(responseObj);
        }
    }



    @Override
    public void onTaskSuccess(ArrayList<?> list) {
        if(callBacks!=null) {
            callBacks.onTaskSuccess(list);
        }
    }

    @Override
    public void onTaskSuccess(boolean success) {
        if(callBacks!=null) {
            callBacks.onTaskSuccess(success);
        }
    }

    @Override
    public void showFullScreenLoader() {
        if(callBacks!=null)callBacks.showFullScreenLoader();
    }

    @Override
    public void onTaskError(String errorMsg) {
        if(callBacks!=null)callBacks.onTaskError(errorMsg);
    }

    @Override
    public void onSessionExpire(String message) {
        if(callBacks!=null)callBacks.onSessionExpire(message);
    }

    @Override
    public void onAppNeedUpdate(String message) {
        if(callBacks!=null)callBacks.onAppNeedUpdate(message);
    }

    @Override
    public void onAppNeedLogin() {
        if(callBacks!=null)callBacks.onAppNeedLogin();
    }

    @Override
    public void onAppNeedForceUpdate(String message) {
        if(callBacks!=null)callBacks.onAppNeedForceUpdate(message);
    }

    @Override
    public void onInternetNotFound() {
        if(callBacks!=null)callBacks.onInternetNotFound();
    }

    @Override
    public void delayAppUpdate() {
        if(callBacks!=null)callBacks.delayAppUpdate();
    }

    @Override
    public void showLoader() {
        if(callBacks!=null)callBacks.showLoader();
    }

    @Override
    public void dismissLoader() {
        if(callBacks!=null)callBacks.dismissLoader();
    }

    @Override
    public void onFragmentDetach(String fragmentTag) {
        if(callBacks!=null)callBacks.onFragmentDetach(fragmentTag);
    }
    @Override
    public void onLoadMore(Object responseObj) {
        if (callBacks!=null) callBacks.onLoadMore(responseObj);
    }

    @Override
    public void onLoadMore(ArrayList<?> list) {
        if (callBacks!=null) callBacks.onLoadMore(list);

    }

}
