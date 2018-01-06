package com.heruijun.reactnativerumtime.base;

import android.content.Context;

/**
 * Created by heruijun on 2017/12/31.
 */

public class RNConfig {

    public static final String VERSION = "1.0";
    private static RNConfig sInstance;
    private static final Object sInstanceLock = new Object();

    public static RNConfig getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (null == sInstance) {
                final Context appContext = context.getApplicationContext();
                sInstance = new RNConfig();
            }
        }
        return sInstance;
    }

}
