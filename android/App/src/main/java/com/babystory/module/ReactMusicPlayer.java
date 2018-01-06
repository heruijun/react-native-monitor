package com.babystory.module;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.View;

import com.babystory.components.AudioWife;
import com.babystory.components.MusicPlayer;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.io.IOException;


/**
 * Created by heruijun on 2018/1/2.
 */

public class ReactMusicPlayer extends SimpleViewManager<MusicPlayer> {

    public static final String REACT_CLASS = "RCTMusicPlayer";
    private Context mReactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected MusicPlayer createViewInstance(ThemedReactContext reactContext) {
        mReactContext = reactContext;
        MusicPlayer musicPlayer = new MusicPlayer(reactContext);
        return musicPlayer;
    }

    @ReactProp(name = "beginPlay")
    public void beginPlay(MusicPlayer view, boolean beginPlay) {
        if(beginPlay) {
            try {
                AssetFileDescriptor fileDescriptor = mReactContext.getAssets().openFd("a.mp3");
                AudioWife.getInstance().init(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength()).useDefaultUi(view);

                AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // do you stuff.
                    }
                });

                AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // get-set-go. Lets dance.
                    }
                });

                AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Your on audio pause stuff.
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
