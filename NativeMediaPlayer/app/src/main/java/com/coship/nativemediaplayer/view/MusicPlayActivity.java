package com.coship.nativemediaplayer.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.MusicBean;
import com.coship.nativemediaplayer.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/11.
 */
public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton musicPlayPauseBtn;
    private TextView musicNameTv;
    private TextView currentProgress;
    private TextView finalProgress;
    private SeekBar musicProgressBar;
    private static int mCurrentPosition = -1;
    private MusicBean mMusicBean;
    private ArrayList<MusicBean> musicList;

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play);

        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra("position", 0);

        initView();
        musicList = DataUtils.getMusicData(this);
        mMusicBean = musicList.get(mCurrentPosition);

        musicNameTv.setText(mMusicBean.getTitle());

        try {
            mediaPlayer.setDataSource(mMusicBean.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicProgressBar.setMax(mediaPlayer.getDuration());
        finalProgress.setText(DataUtils.formatTime(mMusicBean.getDuration()));

        handler.postDelayed(runnable, 500);

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 500);
                if (mediaPlayer != null) {
                    int progress = mediaPlayer.getCurrentPosition();
                    currentProgress.setText(DataUtils.formatTime(progress));
                    musicProgressBar.setProgress(progress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void initView(){

        musicPlayPauseBtn  = (ImageButton) findViewById(R.id.btn_music_play_pause);
        ImageButton musicStopBtn = (ImageButton) findViewById(R.id.btn_music_stop);
        ImageButton musicRewindBtn = (ImageButton) findViewById(R.id.btn_music_forward);
        ImageButton musicPreviousBtn = (ImageButton) findViewById(R.id.btn_music_next);
        musicNameTv = (TextView) findViewById(R.id.tv_music_name);
        currentProgress = (TextView) findViewById(R.id.tv_current_progress);
        finalProgress = (TextView) findViewById(R.id.tv_final_progress);
        musicProgressBar = (SeekBar) findViewById(R.id.sb_music_progress);

        musicPlayPauseBtn.setOnClickListener(this);
        musicStopBtn.setOnClickListener(this);
        musicRewindBtn.setOnClickListener(this);
        musicPreviousBtn.setOnClickListener(this);

        /**
         * 进度条监听
         */
        musicProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /**
         * 歌曲播放结束时监听
         */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {                // 顺序播放
                // TODO Auto-generated method stub
                mCurrentPosition++;    //下一首位置
                Log.d("MusicPlayActivity", mCurrentPosition+"");
                if (mCurrentPosition <= musicList.size() - 1) {
                    mMusicBean = musicList.get(mCurrentPosition);
                    musicNameTv.setText(mMusicBean.getTitle());
                    finalProgress.setText(DataUtils.formatTime(mMusicBean.getDuration()));
                    mp.reset();
                    try {
                        mp.setDataSource(mMusicBean.getPath());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    musicProgressBar.setMax(mediaPlayer.getDuration());
                }
                else {
                    mp.reset();
                    musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_48px));
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_music_play_pause:     //音乐播放暂停
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_48px));
                }else {
                    mediaPlayer.start();
                    musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
                }
                break;
            case R.id.btn_music_stop:     //音乐停止
                musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_48px));
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(mMusicBean.getPath());
                        mediaPlayer.prepare();  // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_music_forward:  //上一首
                musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
                mCurrentPosition--;
                if(mCurrentPosition>=0) {
                    mMusicBean=musicList.get(mCurrentPosition);
                    musicNameTv.setText(mMusicBean.getTitle());
                    finalProgress.setText(DataUtils.formatTime(mMusicBean.getDuration()));
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(mMusicBean.getPath());
                        mediaPlayer.prepare();  // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    musicProgressBar.setMax(mediaPlayer.getDuration());

                }
                else {
                    mCurrentPosition=0;
                    Toast.makeText(this, "没有上一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_music_next:     //下一首
                musicPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
                mCurrentPosition++;
                if(mCurrentPosition<musicList.size()) {
                    mMusicBean=musicList.get(mCurrentPosition);
                    musicNameTv.setText(mMusicBean.getTitle());
                    finalProgress.setText(DataUtils.formatTime(mMusicBean.getDuration()));
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(mMusicBean.getPath());
                        mediaPlayer.prepare();  // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    musicProgressBar.setMax(mediaPlayer.getDuration());
                }
                else{
                    mCurrentPosition = musicList.size() - 1;
                    Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }

    }

}
