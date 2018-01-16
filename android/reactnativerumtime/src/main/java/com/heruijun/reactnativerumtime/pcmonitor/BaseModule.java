package com.heruijun.reactnativerumtime.pcmonitor;

import android.net.Uri;

/**
 * Created by heruijun on 2018/1/16.
 */

public abstract class BaseModule<T> implements Module {
    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        T t = popData();
        if (t == null) {
            return new ResultWrapper("no data for " + getClass().getSimpleName()).toBytes();
        }
        return new ResultWrapper<>(t).toBytes();
    }

    abstract T popData();
}