package com.coship.nativemediaplayer.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by 980560 on 2017/4/20.
 */
public class VideoThumbLoader {
    //创建cache
    private LruCache<String, Bitmap> lruCache;

    @SuppressLint("NewApi")
    public VideoThumbLoader(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory /4;                            //拿到缓存的内存大小
        lruCache = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value){
             //这个方法会在每次存入缓存的时候调用
             return value.getByteCount();
            }
        };
    }

    public void addVideoThumbToCache(String path,Bitmap bitmap){
        if(getVideoThumbToCache(path) == null && bitmap != null){
            //当前地址没有缓存时，就添加
            lruCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path){
        return lruCache.get(path);
    }

    public void showThumbByAsynctack(String path,ImageView imgview){
        if(getVideoThumbToCache(path) == null){
            //异步加载
            new MyBobAsynctack(imgview, path).execute(path);
        }else{
            imgview.setImageBitmap(getVideoThumbToCache(path));
        }
    }

    class MyBobAsynctack extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;

        public MyBobAsynctack(ImageView imageView,String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getVideoThumbnail(params[0], 70, 50,
                    MediaStore.Video.Thumbnails.MICRO_KIND);
            //加入缓存中
            if(getVideoThumbToCache(params[0]) == null){
                addVideoThumbToCache(path, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //通过 Tag可以绑定 图片地址和 imageView，这是解决Gridview加载图片错位的解决办法之一
            if(imgView.getTag().equals(path)){
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}

