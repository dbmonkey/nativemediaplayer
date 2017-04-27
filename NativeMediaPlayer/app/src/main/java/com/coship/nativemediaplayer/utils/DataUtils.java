package com.coship.nativemediaplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.coship.nativemediaplayer.beans.ImageBean;
import com.coship.nativemediaplayer.beans.MusicBean;
import com.coship.nativemediaplayer.beans.VideoBean;

import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/12.
 */
public class DataUtils {
    /**
     * 获取图片数据
     */
    public static ArrayList<ImageBean> getImageData(Context context) {
        ArrayList<ImageBean> imageBeanList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null ) {
            while (cursor.moveToNext()) {
                ImageBean imageBean = new ImageBean();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imageBean.setPath(path);
                imageBeanList.add(imageBean);
            }
            cursor.close();
        }
        return imageBeanList;
    }

    /**
     * 获取音乐数据
     */
    public static ArrayList<MusicBean> getMusicData(Context context) {
        ArrayList<MusicBean> musicBeanList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null ) {
            while (cursor.moveToNext()) {
                MusicBean musicBean = new MusicBean();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                musicBean.setPath(path);
                musicBean.setArtist(artist);
                musicBean.setDuration(duration);
                musicBean.setTitle(title);
                musicBeanList.add(musicBean);
            }
            cursor.close();
        }
        return musicBeanList;
    }

    /**
     * 获取视频数据
     */
    public static ArrayList<VideoBean> getVideoData(Context context) {
        ArrayList<VideoBean> videoBeanList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver()
                .query(uri, null, MediaStore.Video.Media.MIME_TYPE+"!=?", new String[]{"video/dat"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                VideoBean videoBean = new VideoBean();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                videoBean.setPath(path);
                videoBean.setTitle(title);
                videoBean.setDuration(duration);
                videoBeanList.add(videoBean);
            }
            cursor.close();
        }
        return videoBeanList;
    }

    /**
     * 转换时间格式
     */
    public static String formatTime(int time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}
