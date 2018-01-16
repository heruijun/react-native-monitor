package com.heruijun.reactnativerumtime.pcmonitor;

import android.net.Uri;

import com.heruijun.reactnativerumtime.util.GsonUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by heruijun on 2018/1/16.
 */

public interface Module {

    public byte[] process(String path, Uri uri) throws Throwable;

    public static class ResultWrapper<T> {

        public static final int SUCCESS = 1;
        public static final int DEFAULT_FAIL = 0;
        public T data;
        public int code;
        public String message;

        public ResultWrapper(int code, String message, T data) {
            this.data = data;
            this.code = code;
            this.message = message;
        }

        public ResultWrapper(String message) {
            this.data = null;
            this.code = DEFAULT_FAIL;
            this.message = message;
        }

        public ResultWrapper(T data) {
            this.data = data;
            this.code = SUCCESS;
            this.message = "success";
        }

        public byte[] toBytes() throws UnsupportedEncodingException {
            return GsonUtil.toJson(this).getBytes("UTF-8");
        }
    }
    
}
