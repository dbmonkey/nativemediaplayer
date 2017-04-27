package com.coship.nativemediaplayer.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.ImageBean;
import com.coship.nativemediaplayer.beans.VideoBean;
import com.coship.nativemediaplayer.utils.DataUtils;

import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/14.
 */
public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener{

    private VideoView videoView ;
    private TextView currentProgress;
    private TextView finalProgress;
    private SeekBar videoProgressBar;
    private ImageButton videoPlayPauseBtn;
    private ImageButton videoFasterBtn;
    private ImageButton previousBtn;
    private ImageButton videoSlowerBtn;
    private ImageButton nextBtn;
    private static int mCurrentPosition = -1;

    private int mCurrentProgress;

    private VideoBean videoBean;
    private ArrayList<VideoBean> videoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra("position", 0);

        initView();

        videoView.setVideoPath(videoBean.getPath());
        videoView.start();

        videoProgressBar.setMax(videoBean.getDuration());

        finalProgress.setText(DataUtils.formatTime(videoBean.getDuration()));

        handler.postDelayed(updateRnable, 500);

        videoView.requestFocus();
        videoView.setOnClickListener(new View.OnClickListener() {    //显示按钮控件
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.video_layout_visibility);
                layout.setVisibility(View.VISIBLE);
                videoView.setFocusable(false);
                videoPlayPauseBtn.requestFocus();
            }
        });
    }

    Handler handler = new Handler();
    Runnable updateRnable = new Runnable() {    //更新当前时间和进度条
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 500);
                if (videoView != null) {
                    int progress = videoView.getCurrentPosition();
                    currentProgress.setText(DataUtils.formatTime(progress));
                    videoProgressBar.setProgress(progress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Runnable fasterRnable = new Runnable() {    //持续快进
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 1000);
                if (videoView != null) {
                    mCurrentProgress = videoView.getCurrentPosition();
                    mCurrentProgress += 5000;
                    if (mCurrentProgress <= videoBean.getDuration()){
                        videoView.seekTo(mCurrentProgress);
                    }
                    else {
                        mCurrentProgress = videoBean.getDuration();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Runnable slowerRnable = new Runnable() {    //持续快退
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 1000);
                if (videoView != null) {
                    mCurrentProgress = videoView.getCurrentPosition();
                    mCurrentProgress -= 5000;
                    if (mCurrentProgress >= 0){
                        videoView.seekTo(mCurrentProgress);
                    }
                    else {
                        mCurrentProgress = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void initView(){
        videoView = (VideoView) findViewById(R.id.video_play_vv);
        currentProgress = (TextView) findViewById(R.id.tv_current_progress);
        finalProgress = (TextView) findViewById(R.id.tv_final_progress);
        videoProgressBar = (SeekBar) findViewById(R.id.sb_video_progress);
        videoPlayPauseBtn = (ImageButton) findViewById(R.id.btn_video_play_pause);
        previousBtn = (ImageButton) findViewById(R.id.btn_video_previous);
        nextBtn = (ImageButton) findViewById(R.id.btn_video_next);
        videoFasterBtn = (ImageButton) findViewById(R.id.btn_video_faster);
        videoSlowerBtn = (ImageButton) findViewById(R.id.btn_video_slower);

        videoList = DataUtils.getVideoData(this);
        videoBean = videoList.get(mCurrentPosition);

        videoPlayPauseBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        videoFasterBtn.setOnClickListener(this);
        videoSlowerBtn.setOnClickListener(this);

        /**
         * 进度条监听
         */
        videoProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    remove();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_video_play_pause:     //播放/暂停
                playOrPause();
                break;
            case R.id.btn_video_previous:       //上一个
                previousVideo();
                break;
            case R.id.btn_video_next:           //下一个
                nextVideo();
                break;
            case R.id.btn_video_faster:         //持续快进10秒
                remove();
                handler.postDelayed(fasterRnable, 1000);
                break;
            case R.id.btn_video_slower:         //持续快退10秒
                remove();
                handler.postDelayed(slowerRnable, 1000);
                break;
            default:
                break;
        }
    }

    private void remove() {    //删除fasterRnable和slowerRnable，使持续快进和持续快退停止运行
        if (fasterRnable != null || slowerRnable != null) {
            handler.removeCallbacks(fasterRnable);
            handler.removeCallbacks(slowerRnable);
        }
    }

    private void playOrPause() {    //视频播放或暂停
        if (videoView.isPlaying()) {
            videoView.pause();
            videoPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.play_48px));
        }
        else if (!videoView.isPlaying()) {
            videoView.start();
            videoPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
        }
        remove();
    }

    private void previousVideo() {    //上一个视频
        videoPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
        mCurrentPosition--;
        if (mCurrentPosition >= 0) {
            videoBean = videoList.get(mCurrentPosition);
            finalProgress.setText(DataUtils.formatTime(videoBean.getDuration()));
            videoView.setVideoPath(videoBean.getPath());
            videoView.start();
            videoProgressBar.setMax(videoBean.getDuration());
        }
        else {
            mCurrentPosition = 0;
            Toast.makeText(this, "已经是第一个视频了！", Toast.LENGTH_SHORT).show();
        }
        remove();
    }

    private void nextVideo() {    //下一个视频
        videoPlayPauseBtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_48px));
        mCurrentPosition++;
        if (mCurrentPosition <= videoList.size() - 1) {
            videoBean = videoList.get(mCurrentPosition);
            finalProgress.setText(DataUtils.formatTime(videoBean.getDuration()));
            videoView.setVideoPath(videoBean.getPath());
            videoView.start();
            videoProgressBar.setMax(videoBean.getDuration());
        }
        else {
            mCurrentPosition = videoList.size() - 1;
            Toast.makeText(this, "已经是最后一个视频了！", Toast.LENGTH_SHORT).show();
        }
        remove();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            //如果焦点在进度条上，按向下键时，焦点退回控制按钮上
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && videoProgressBar.hasFocus()) {
                videoPlayPauseBtn.requestFocus();
                LinearLayout layout = (LinearLayout) findViewById(R.id.video_layout_visibility);
                layout.setVisibility(View.VISIBLE);
                videoView.setFocusable(true);
                return true;
            }
            //如果焦点在控制按钮上，按向下键时按钮控件隐藏
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && (videoPlayPauseBtn.hasFocus()
                    || previousBtn.hasFocus() || nextBtn.hasFocus() || videoFasterBtn.hasFocus()
                    || videoSlowerBtn.hasFocus()) && !videoProgressBar.hasFocus()) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.video_layout_visibility);
                layout.setVisibility(View.GONE);
                videoView.setFocusable(true);
                return true;
            }
        }
            return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
        if (updateRnable != null) {
            handler.removeCallbacks(updateRnable);
            updateRnable = null;
        }
        if (fasterRnable != null) {
            handler.removeCallbacks(fasterRnable);
            fasterRnable = null;
        }
        if (slowerRnable != null) {
            handler.removeCallbacks(slowerRnable);
            slowerRnable = null;
        }

    }
}
