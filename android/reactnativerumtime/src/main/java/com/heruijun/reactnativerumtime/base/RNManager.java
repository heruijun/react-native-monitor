package com.heruijun.reactnativerumtime.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.heruijun.reactnativerumtime.inc.RNEvent;
import com.heruijun.reactnativerumtime.pcmonitor.ClientServer;
import com.heruijun.reactnativerumtime.pcmonitor.Router;
import com.heruijun.reactnativerumtime.util.RNLog;
import com.heruijun.reactnativerumtime.viewcrawler.ViewCrawler;

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
    private static final int DEFAULT_PORT = 5391;
    private static boolean isMonitorOnPC = false;
    private static ClientServer sClientServer;

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

    public static synchronized void monitorOnPC(Context context) {
        monitorOnPC(context, DEFAULT_PORT);
    }

    public static synchronized void monitorOnPC(Context context, int port) {
        if (isMonitorOnPC) {
            return;
        }
        isMonitorOnPC = true;
        if (context == null) {
            throw new IllegalStateException("context can not be null.");
        }
        Context applicationContext = context.getApplicationContext();
        Router.get().init(applicationContext);
        initServer(applicationContext, port);
    }

    private static void initServer(Context context, int port) {
        sClientServer = new ClientServer(port);
        sClientServer.start();
        RNLog.d("addresslog: ", getAddressLog(context, port));
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

    private static String getAddressLog(Context context, int port) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager != null ? wifiManager.getConnectionInfo().getIpAddress() : 0;
        @SuppressLint("DefaultLocale") final String formattedIpAddress = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "Open dashboard [ http://" + formattedIpAddress + ":" + port + " ] in your browser , if can not open it , make sure device and pc are on the same network segment";
    }
}
