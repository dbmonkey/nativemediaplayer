package com.coship.nativemediaplayer.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.MusicBean;

import java.util.List;

/**
 * Created by 980560 on 2017/4/12.
 */
public class MusicAdapter extends BaseAdapter {

    private Context context;
    private List<MusicBean> musicList;
    public MusicAdapter(Context context, List<MusicBean> list) {
        this.musicList = list;
        this.context = context;

    }
    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicBean bean = (MusicBean) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            //引入布局
            convertView = View.inflate(context, R.layout.music_item, null);
            //实例化对象
            holder.number = (TextView) convertView.findViewById(R.id.music_number);
            holder.title = (TextView) convertView.findViewById(R.id.music_name);
            holder.artist = (TextView) convertView.findViewById(R.id.music_artist);
            holder.duration = (TextView) convertView.findViewById(R.id.music_duration);
            //将holder存储在convertView中
            convertView.setTag(holder);
        }
        else {
            //重新获取holder
            holder = (ViewHolder) convertView.getTag();
        }

        //给控件赋值
        holder.number.setText(position + 1 + "");
        holder.title.setText(musicList.get(position).getTitle());
        holder.artist.setText(musicList.get(position).getArtist());
        holder.duration.setText(DataUtils.formatTime(musicList.get(position).getDuration()));
        Log.d("TAG", "getView: "+DataUtils.formatTime(musicList.get(position).getDuration()));

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView artist;
        TextView number;
        TextView duration;
    }
}
