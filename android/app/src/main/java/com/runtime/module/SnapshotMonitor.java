package com.runtime.module;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.runtime.base.RNApplication;
import com.runtime.inc.RNEvent;

/**
 * Created by heruijun on 2018/1/1.
 */

public class SnapshotMonitor extends ReactContextBaseJavaModule {

    public SnapshotMonitor(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SnapshotMonitor";
    }

    @ReactMethod
    public void show() {
        RNApplication.getInstance().getRNManager().snapshot(new RNEvent() {
            @Override
            public void onSnapResult(String result) {
                DialogUtils.showConfirmDialog(getCurrentActivity(), result);
            }
        });
    }
}
