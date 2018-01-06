package com.heruijun.reactnativerumtime.base;

import android.app.Application;

/**
 * Created by heruijun on 2018/1/1.
 */

public class RNApplication extends Application {

    private static RNApplication instance;
    private RNManager mRNManager;

    public static RNApplication getInstance() {
        return instance;
    }

    public RNManager getRNManager() {
        return mRNManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mRNManager = RNManager.getInstance(getApplicationContext());
    }
}
