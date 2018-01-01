package com.runtime.viewcrawler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;

import com.runtime.util.RNLog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by heruijun on 2018/1/1.
 */

public class ViewSnapshot {

    private UIThreadSet<Activity> mLiveActivities;
    private final RootViewFinder mRootViewFinder;
    private final Handler mMainThreadHandler;
    private static final String LOGTAG = "RN.Snapshot";

    public ViewSnapshot(Context context) {
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        mRootViewFinder = new RootViewFinder();
    }

    public String snapshots(UIThreadSet<Activity> liveActivities) {
        String result = "";
        mRootViewFinder.findInActivities(liveActivities);
        final FutureTask<List<RootViewInfo>> infoFuture = new FutureTask<List<RootViewInfo>>(mRootViewFinder);
        mMainThreadHandler.post(infoFuture);
        List<RootViewInfo> infoList = Collections.<RootViewInfo>emptyList();

        try {
            infoList = infoFuture.get(1, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            RNLog.d(LOGTAG, "Screenshot interrupted, no screenshot will be sent.", e);
        } catch (final TimeoutException e) {
            RNLog.i(LOGTAG, "Screenshot took more than 1 second to be scheduled and executed. No screenshot will be sent.", e);
        } catch (final ExecutionException e) {
            RNLog.e(LOGTAG, "Exception thrown during screenshot attempt", e);
        }

        final int infoCount = infoList.size();
        for (int i = 0; i < infoCount; i++) {
//            final RootViewInfo info = infoList.get(i);
//            Log.e("screenshot", info.screenshot.bitmapToBase64());
            final RootViewInfo info = infoList.get(0);
            result = info.screenshot.bitmapToBase64();
        }
        return result;
    }

    private static class RootViewFinder implements Callable<List<RootViewInfo>> {

        private final DisplayMetrics mDisplayMetrics;
        private final List<RootViewInfo> mRootViews;
        private final CachedBitmap mCachedBitmap;
        private UIThreadSet<Activity> mLiveActivities;
        private final int mClientDensity = DisplayMetrics.DENSITY_DEFAULT;

        public RootViewFinder() {
            this.mDisplayMetrics = new DisplayMetrics();
            this.mRootViews = new ArrayList<>();
            this.mCachedBitmap = new CachedBitmap();
        }

        public void findInActivities(UIThreadSet<Activity> liveActivities) {
            mLiveActivities = liveActivities;
        }

        @Override
        public List<RootViewInfo> call() throws Exception {
            mRootViews.clear();

            final Set<Activity> liveActivities = mLiveActivities.getAll();

            for (final Activity a : liveActivities) {
                final String activityName = a.getClass().getCanonicalName();
                final View rootView = a.getWindow().getDecorView().getRootView();
                a.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                final RootViewInfo info = new RootViewInfo(activityName, rootView);
                mRootViews.add(info);
            }

            final int viewCount = mRootViews.size();
            for (int i = 0; i < viewCount; i++) {
                final RootViewInfo info = mRootViews.get(i);
                takeScreenshot(info);
            }

            return mRootViews;
        }

        /**
         * 截取屏幕快照
         *
         * @param info
         */
        public void takeScreenshot(final RootViewInfo info) {
            final View rootView = info.rootView;
            Bitmap rawBitmap = null;

            try {
                final Method createSnapshot = View.class.getDeclaredMethod("createSnapshot", Bitmap.Config.class, Integer.TYPE, Boolean.TYPE);
                createSnapshot.setAccessible(true);
                rawBitmap = (Bitmap) createSnapshot.invoke(rootView, Bitmap.Config.RGB_565, Color.WHITE, false);
            } catch (final NoSuchMethodException e) {
                RNLog.v(LOGTAG, "Can't call createSnapshot, will use drawCache", e);
            } catch (final IllegalArgumentException e) {
                RNLog.d(LOGTAG, "Can't call createSnapshot with arguments", e);
            } catch (final InvocationTargetException e) {
                RNLog.e(LOGTAG, "Exception when calling createSnapshot", e);
            } catch (final IllegalAccessException e) {
                RNLog.e(LOGTAG, "Can't access createSnapshot, using drawCache", e);
            } catch (final ClassCastException e) {
                RNLog.e(LOGTAG, "createSnapshot didn't return a bitmap?", e);
            }

            Boolean originalCacheState = null;
            try {
                if (null == rawBitmap) {
                    originalCacheState = rootView.isDrawingCacheEnabled();
                    rootView.setDrawingCacheEnabled(true);
                    rootView.buildDrawingCache(true);
                    rawBitmap = rootView.getDrawingCache();
                }
            } catch (final RuntimeException e) {
                RNLog.v(LOGTAG, "Can't take a bitmap snapshot of view " + rootView + ", skipping for now.", e);
            }

            float scale = 1.0f;
            if (null != rawBitmap) {
                final int rawDensity = rawBitmap.getDensity();

                if (rawDensity != Bitmap.DENSITY_NONE) {
                    scale = ((float) mClientDensity) / rawDensity;
                }

                final int rawWidth = rawBitmap.getWidth();
                final int rawHeight = rawBitmap.getHeight();
                final int destWidth = (int) ((rawBitmap.getWidth() * scale) + 0.5);
                final int destHeight = (int) ((rawBitmap.getHeight() * scale) + 0.5);

                if (rawWidth > 0 && rawHeight > 0 && destWidth > 0 && destHeight > 0) {
                    mCachedBitmap.recreate(destWidth, destHeight, mClientDensity, rawBitmap);
                }
            }

            if (null != originalCacheState && !originalCacheState) {
                rootView.setDrawingCacheEnabled(false);
            }
            info.scale = scale;
            info.screenshot = mCachedBitmap;
        }
    }

    private static class RootViewInfo {
        public final String activityName;
        public final View rootView;
        public CachedBitmap screenshot;
        public float scale;

        public RootViewInfo(String activityName, View rootView) {
            this.activityName = activityName;
            this.rootView = rootView;
            this.screenshot = null;
            this.scale = 1.0f;
        }
    }

    private static class CachedBitmap {
        private Bitmap mCached;
        private final Paint mPaint;

        public CachedBitmap() {
            mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
            mCached = null;
        }

        public synchronized void recreate(int width, int height, int destDensity, Bitmap source) {
            if (null == mCached || mCached.getWidth() != width || mCached.getHeight() != height) {
                try {
                    mCached = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                } catch (final OutOfMemoryError e) {
                    mCached = null;
                }

                if (null != mCached) {
                    mCached.setDensity(destDensity);
                }
            }

            if (null != mCached) {
                final Canvas scaledCanvas = new Canvas(mCached);
                scaledCanvas.drawBitmap(source, 0, 0, mPaint);
            }
        }

        // Writes a QUOTED base64 string (or the string null) to the output stream
        public synchronized void writeBitmapJSON(Bitmap.CompressFormat format, int quality, OutputStream out)
                throws IOException {
            if (null == mCached || mCached.getWidth() == 0 || mCached.getHeight() == 0) {
                out.write("null".getBytes());
            } else {
                out.write('"');
                final Base64OutputStream imageOut = new Base64OutputStream(out, Base64.NO_WRAP);
                mCached.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
                imageOut.flush();
                out.write('"');
            }
        }

        public synchronized String bitmapToBase64() {
            String result = null;
            ByteArrayOutputStream baos = null;
            try {
                if (mCached != null) {
                    baos = new ByteArrayOutputStream();
                    mCached.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    baos.flush();
                    baos.close();

                    byte[] bitmapBytes = baos.toByteArray();
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                } else {
                    return "none";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null) {
                        baos.flush();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
