package com.heruijun.reactnativerumtime.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;

/**
 * Created by heruijun on 2018/1/16.
 */

public class GsonUtil {
    private static Gson sGson = new GsonBuilder().create();

    public static String toJson(Object t) {
        return sGson.toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        return sGson.fromJson(json, clz);
    }

    public static <T> T fromJson(Reader reader, Class<T> clz) {
        return sGson.fromJson(reader, clz);
    }
}