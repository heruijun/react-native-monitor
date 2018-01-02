package com.babystory.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.babystory.R;

/**
 * Created by heruijun on 2018/1/2.
 */

public class MusicPlayer extends LinearLayout {

    private Context mContext;
    private ImageView mPlayView;
    private ImageView mPauseView;
    private SeekBar mSeekBar;
    private TextView mPlayTime;

    public MusicPlayer(Context context) {
        this(context, null);
        init();
    }

    public MusicPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playerUi = inflater.inflate(R.layout.aw_player, this);
        mPlayView = playerUi.findViewById(R.id.play);
        mPauseView = playerUi.findViewById(R.id.pause);
        mSeekBar = (SeekBar) playerUi.findViewById(R.id.media_seekbar);
        mPlayTime = (TextView) playerUi.findViewById(R.id.playback_time);
    }

    public View getPlayView() {
        return mPlayView;
    }

    public View getPauseView() {
        return mPauseView;
    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    public TextView getPlaytime() {
        return mPlayTime;
    }

}
