package com.heruijun.reactnativerumtime.pcmonitor;

import android.content.Context;
import android.net.Uri;

import java.util.Map;

/**
 * Created by heruijun on 2018/1/16.
 */

public class Router {

    private Map<String, Module> mRouteModules;

    private Router() {

    }

    private static class InstanceHolder {
        private static Router sInstance = new Router();
    }

    public static Router get() {
        return InstanceHolder.sInstance;
    }

    public void init(Context context) {
        mRouteModules = new android.support.v4.util.ArrayMap<>();
        AssetsModule assetsModule = new AssetsModule(context, "monitor");
        mRouteModules.put("assets", assetsModule);
    }

    public byte[] process(Uri uri) throws Throwable {
        String moduleName = uri.getPath();
        Module module = mRouteModules.get(moduleName);
        if (module == null) {
            return mRouteModules.get("assets").process(moduleName, uri);
        }
        return module.process(moduleName, uri);
    }

}
