package com.coship.nativemediaplayer.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.VideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 980560 on 2017/4/14.
 */
public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<VideoBean> videoList;

    public VideoAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.videoList = list;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            //引入布局
            convertView = View.inflate(context, R.layout.video_item, null);
            //实例化对象
            holder.image = (ImageView) convertView.findViewById(R.id.video_image_iv);
            holder.title = (TextView) convertView.findViewById(R.id.video_title_tv);
            holder.duration = (TextView) convertView.findViewById(R.id.video_duration_tv);
            //将holder存储在convertView中
            convertView.setTag(holder);
        }
        else {
            //重新获取holder
            holder = (ViewHolder) convertView.getTag();
        }

        //给控件赋值
        String path = videoList.get(position).getPath();
        holder.image.setTag(path);//绑定imageview
        VideoThumbLoader loader = new VideoThumbLoader();
        loader.showThumbByAsynctack(path, holder.image);
        holder.title.setText(videoList.get(position).getTitle());
        holder.duration.setText(DataUtils.formatTime(videoList.get(position).getDuration()));

        Log.d("TITLE", "title: "+videoList.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView  title;
        TextView  duration;
    }
}
