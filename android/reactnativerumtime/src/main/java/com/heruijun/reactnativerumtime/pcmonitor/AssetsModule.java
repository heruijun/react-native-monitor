package com.heruijun.reactnativerumtime.pcmonitor;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by heruijun on 2018/1/16.
 */

public class AssetsModule implements Module {

    private AssetManager mAssets;
    private String mPreffix;

    public AssetsModule(Context context, String mPreffix) {
        this.mPreffix = mPreffix;
        this.mAssets = context.getResources().getAssets();
    }

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        String fileName = mPreffix + "/" + uri.getPath();
        return loadContent(fileName, mAssets);
    }

    private static byte[] loadContent(String fileName, AssetManager assetManager) throws IOException {
        InputStream input = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            input = assetManager.open(fileName);
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                output.write(buffer, 0, size);
            }
            output.flush();
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
