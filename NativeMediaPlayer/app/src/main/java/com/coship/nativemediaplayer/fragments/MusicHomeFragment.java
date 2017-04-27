package com.coship.nativemediaplayer.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.MusicBean;
import com.coship.nativemediaplayer.utils.DataUtils;
import com.coship.nativemediaplayer.utils.MusicAdapter;
import com.coship.nativemediaplayer.utils.MyListView;
import com.coship.nativemediaplayer.view.MusicPlayActivity;

import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/7.
 */
public class MusicHomeFragment extends Fragment {
    private ListView musicList;
    MusicAdapter adapter;
    ArrayList<MusicBean> musicBeanList;
    int index;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_home, container, false);
        musicList = (MyListView) view.findViewById(R.id.music_list_lv);
        musicList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Button btnMusic = (Button) getActivity().findViewById(R.id.btn_music);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    btnMusic.requestFocus();
                    return true;
                }
                if (btnMusic.hasFocus() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    musicList.getChildAt(index).requestFocus();
                    return true;
                }
                return false;
            }
        });
        musicList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //自定义GetMusicDataTask去获取数据
        new GetMusicDataTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        //歌曲列表点击事件
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent2 = new Intent(getActivity(),MusicPlayActivity.class);
                intent2.putExtra("position",position);
                startActivity(intent2);
            }
        });
    }

    /**
     * 继承一个AsyncTask类去获取数据
     */
    public class GetMusicDataTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            musicBeanList = DataUtils.getMusicData(getActivity());
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new MusicAdapter(getActivity(),musicBeanList);
            musicList.setAdapter(adapter);
        }
    }

}
