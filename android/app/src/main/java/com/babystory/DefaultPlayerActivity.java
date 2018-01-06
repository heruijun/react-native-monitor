package com.babystory;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.babystory.components.MusicPlayer;
import com.babystory.components.AudioWife;

import java.io.IOException;

/**
 * Created by heruijun on 2018/1/2.
 */

public class DefaultPlayerActivity extends AppCompatActivity {

    private static final String TAG = DefaultPlayerActivity.class.getSimpleName();
    private Context mContext;
    private MusicPlayer mMusicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awd_default_player);
        mContext = DefaultPlayerActivity.this;
        mMusicPlayer = findViewById(R.id.music_player);

        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("a.mp3");
            AudioWife.getInstance().init(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength()).useDefaultUi(mMusicPlayer);

            AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getBaseContext(), "Completed", Toast.LENGTH_SHORT).show();
                    // do you stuff.
                }
            });

            AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Play", Toast.LENGTH_SHORT).show();
                    // get-set-go. Lets dance.
                }
            });

            AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Pause", Toast.LENGTH_SHORT).show();
                    // Your on audio pause stuff.
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // when done playing, release the resources
        // AudioWife.getInstance().release();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioWife.getInstance().release();
    }
}
