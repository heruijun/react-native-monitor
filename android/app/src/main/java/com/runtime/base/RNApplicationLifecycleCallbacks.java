package com.runtime.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by heruijun on 2017/12/31.
 */

public class RNApplicationLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private final RNManager mRNManager;
    private final RNConfig mConfig;

    public RNApplicationLifecycleCallbacks(RNManager mRNManager, RNConfig mConfig) {
        this.mRNManager = mRNManager;
        this.mConfig = mConfig;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
