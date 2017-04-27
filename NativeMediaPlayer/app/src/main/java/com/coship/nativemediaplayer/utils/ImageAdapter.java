package com.coship.nativemediaplayer.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.ImageBean;

import java.util.List;

/**
 * Created by 980560 on 2017/4/17.
 */
public class ImageAdapter extends BaseAdapter {
    private List<ImageBean> imageBeanList;
    private Context context;

    public ImageAdapter(Context context, List<ImageBean> list) {
        this.context = context;
        this.imageBeanList = list;
    }

    @Override
    public int getCount() {
        return imageBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageBeanList.get(position);
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
            convertView = View.inflate(context, R.layout.image_item, null);
            //实例化对象
            holder.image = (ImageView) convertView.findViewById(R.id.image_item_iv);
            //将holder存储在convertView中
            convertView.setTag(holder);
        }
        else {
            //重新获取holder
            holder = (ViewHolder) convertView.getTag();
        }

        //给控件赋值
        Glide.with(context).load(imageBeanList.get(position).getPath()).thumbnail(0.2f).into(holder.image);
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
