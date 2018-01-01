package com.runtime.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.runtime.inc.RNEvent;
import com.runtime.viewcrawler.ViewCrawler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heruijun on 2017/12/31.
 */

public class RNManager {

    private final Map<String, String> mDeviceInfo;
    private static final Map<Context, RNManager> sInstanceMap = new HashMap<>();
    private RNApplicationLifecycleCallbacks mRNApplicationLifecycleCallbacks;
    private final RNConfig mConfig;
    private final Context mContext;
    private ViewCrawler mViewCrawler;

    public RNManager(Context mContext, RNConfig config) {
        this.mContext = mContext;
        this.mConfig = config;
        final Map<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("#android_lib_version", RNConfig.VERSION);
        deviceInfo.put("#android_os", "Android");
        mDeviceInfo = Collections.unmodifiableMap(deviceInfo);
        registerActivityLifecycleCallbacks();
        registerViewCrawler();
    }

    public static RNManager getInstance(Context context) {
        synchronized (sInstanceMap) {
            final Context appContext = context.getApplicationContext();
            RNManager instance = sInstanceMap.get(appContext);
            if (null == instance) {
                instance = new RNManager(appContext, RNConfig.getInstance(context));
                sInstanceMap.put(appContext, instance);
            }
            return instance;
        }
    }

    void registerActivityLifecycleCallbacks() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mContext.getApplicationContext() instanceof Application) {
                final Application app = (Application) mContext.getApplicationContext();
                mRNApplicationLifecycleCallbacks = new RNApplicationLifecycleCallbacks(this, mConfig);
                app.registerActivityLifecycleCallbacks(mRNApplicationLifecycleCallbacks);
            }
        }
    }

    private void registerViewCrawler() {
        mViewCrawler = new ViewCrawler(mContext, this);
    }

    public void snapshot(RNEvent event) {
        mViewCrawler.snapshot(event);
    }

    public Map<String, String> getDeviceInfo() {
        return mDeviceInfo;
    }
}
