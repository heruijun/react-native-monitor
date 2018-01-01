package com.runtime.module;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.babystory.R;

/**
 * Created by heruijun on 2018/1/1.
 */

public class DialogUtils {

    public static void showConfirmDialog(Context context, String base64Image) {
        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.rn_dialog, null);
        ImageView img = (ImageView) view.findViewById(R.id.monitor_image);
        byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

}
