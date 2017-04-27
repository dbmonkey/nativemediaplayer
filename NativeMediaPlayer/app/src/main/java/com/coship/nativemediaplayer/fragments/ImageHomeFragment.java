package com.coship.nativemediaplayer.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.coship.nativemediaplayer.utils.MyGridView;
import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.ImageBean;
import com.coship.nativemediaplayer.utils.DataUtils;
import com.coship.nativemediaplayer.utils.ImageAdapter;
import com.coship.nativemediaplayer.view.ImagePlayActivity;

import java.util.List;

/**
 * Created by 980560 on 2017/4/17.
 */
public class ImageHomeFragment extends Fragment {
    GridView imageList;
    ImageAdapter adapter;
    List<ImageBean> imageBeanList;
    int index;  //当前选中的item的索引
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_home, container, false);
        imageList = (MyGridView) view.findViewById(R.id.image_list_gv);
        imageList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Button btnImage = (Button) getActivity().findViewById(R.id.btn_image);

                //按返回键，焦点退回图片按钮
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    btnImage.requestFocus();
                    return true;
                }

                //焦点在最上面一行时，按向上键焦点退回图片按钮
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((index/4) == 0 && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        btnImage.requestFocus();
                        return true;
                    }
                }

                //焦点退回到图片按钮时，当按下向下键，重新选中上一次选中的item
                if (btnImage.hasFocus() && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        imageList.getChildAt(index).requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        imageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;    //获得选中item的索引
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new GetImageDataTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ImagePlayActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    /**
     * 继承一个AsyncTask类去获取数据
     */
    public class GetImageDataTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            imageBeanList = DataUtils.getImageData(getActivity());
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ImageAdapter(getActivity(),imageBeanList);
            imageList.setAdapter(adapter);
        }
    }
}
