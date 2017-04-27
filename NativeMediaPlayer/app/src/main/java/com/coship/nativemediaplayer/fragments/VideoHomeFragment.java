package com.coship.nativemediaplayer.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.VideoBean;
import com.coship.nativemediaplayer.utils.DataUtils;
import com.coship.nativemediaplayer.utils.MyGridView;
import com.coship.nativemediaplayer.utils.VideoAdapter;
import com.coship.nativemediaplayer.view.VideoPlayActivity;

import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/14.
 */
public class VideoHomeFragment extends Fragment {
    GridView videoList;
    VideoAdapter adapter;
    ArrayList<VideoBean> videoBeanList;
    int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_home, container, false);
        videoList = (MyGridView) view.findViewById(R.id.video_list_gv);
        videoList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Button btnVideo = (Button) getActivity().findViewById(R.id.btn_video);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    btnVideo.requestFocus();
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((index/4) == 0 && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        btnVideo.requestFocus();
                        return true;
                    }
                }

                if (btnVideo.hasFocus() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    videoList.getChildAt(index).requestFocus();
                    return true;
                }
                return false;
            }
        });
//        if (!videoList.hasFocus()) {
//            btnVideo.requestFocus();
//        }
        videoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //自定义GetVideoDataTask去获取数据
        new GetVideoDataTask().execute();

        return view;
    }

//    View.OnKeyListener listener = new View.OnKeyListener() {
//        @Override
//        public boolean onKey(View v, int keyCode, KeyEvent event) {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                btnVideo.requestFocus();
//                return true;
//            }
//            return false;
//        }
//    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    /**
     * 继承一个AsyncTask类去获取数据
     */
    public class GetVideoDataTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            videoBeanList= DataUtils.getVideoData(getActivity());
            adapter = new VideoAdapter(getActivity(),videoBeanList);
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            videoList.setAdapter(adapter);
        }
    }
}

