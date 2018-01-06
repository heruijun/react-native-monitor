package com.heruijun.reactnativerumtime.viewcrawler;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.heruijun.reactnativerumtime.base.RNManager;
import com.heruijun.reactnativerumtime.inc.RNEvent;

import java.util.Map;

/**
 * Created by heruijun on 2017/12/31.
 */

public class ViewCrawler {

    private final Map<String, String> mDeviceInfo;
    private Context mContext;
    private ViewSnapshot mViewSnapshot;
    private EditState mEditState;
    private final ViewCrawlerHandler mMessageThreadHandler;
    private static final int MESSAGE_SEND_STATE_FOR_EDITING = 2;
    private String mSnapResult;
    private RNEvent event;

    public ViewCrawler(Context context, RNManager rnManager) {
        mContext = context;
        mDeviceInfo = rnManager.getDeviceInfo();
        mEditState = new EditState();
        mViewSnapshot = new ViewSnapshot(mContext);
        final HandlerThread thread = new HandlerThread(ViewCrawler.class.getCanonicalName());
        thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mMessageThreadHandler = new ViewCrawlerHandler(thread.getLooper());
        final Application app = (Application) context.getApplicationContext();
        app.registerActivityLifecycleCallbacks(new LifecycleCallbacks());
    }

    public void snapshot(RNEvent event) {
        this.event = event;
        final Message msg = mMessageThreadHandler.obtainMessage();
        msg.what = ViewCrawler.MESSAGE_SEND_STATE_FOR_EDITING;
        mMessageThreadHandler.sendMessage(msg);
    }

    private class ViewCrawlerHandler extends Handler {

        public ViewCrawlerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case MESSAGE_SEND_STATE_FOR_EDITING:
                    mSnapResult = mViewSnapshot.snapshots(mEditState);
                    // Log.e("snap result", mSnapResult);
                    if(event != null) {
                        event.onSnapResult(mSnapResult);
                    }
                    break;
            }
        }
    }

    private class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            mEditState.add(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mEditState.remove(activity);
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
}
