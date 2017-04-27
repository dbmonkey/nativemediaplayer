package com.coship.nativemediaplayer.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.beans.ImageBean;
import com.coship.nativemediaplayer.utils.DataUtils;

import java.util.ArrayList;

/**
 * Created by 980560 on 2017/4/18.
 */
public class ImagePlayActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imagePlayIv;
    private ImageButton enlargeBtn;
    private ImageButton lessonBtn;
    private ImageButton scaleBtn;
    private ImageButton previousBtn;
    private ImageButton nextBtn;
    private ArrayList<ImageBean> imageList;
    private ImageBean imageBean;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private int   angel = 0;

    private static int mCurrentPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_play);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        if(position != mCurrentPosition) {
            mCurrentPosition = position;
        }

        //初始化控件
        init();
        //展示图片
        displayImage(imageBean.getPath());
    }

    public void init() {

        imagePlayIv = (ImageView) findViewById(R.id.image_play_iv);
        enlargeBtn  = (ImageButton) findViewById(R.id.imgbtn_image_enlarge);
        lessonBtn   = (ImageButton) findViewById(R.id.imgbtn_image_lesson);
        scaleBtn    = (ImageButton) findViewById(R.id.imgbtn_image_scale);
        previousBtn = (ImageButton) findViewById(R.id.imgbtn_image_previous);
        nextBtn     = (ImageButton) findViewById(R.id.imgbtn_image_next);

        imagePlayIv.setOnClickListener(this);
        enlargeBtn.setOnClickListener(this);
        lessonBtn.setOnClickListener(this);
        scaleBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        imageList = DataUtils.getImageData(this);
        imageBean = imageList.get(mCurrentPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_image_enlarge:
                enlarge();
                break;
            case R.id.imgbtn_image_lesson:
                lesson();
                break;
            case R.id.imgbtn_image_scale:
                scale();
                break;
            case R.id.imgbtn_image_previous:
                previousImg();
                break;
            case R.id.imgbtn_image_next:
                nextImg();
                break;
            case R.id.image_play_iv:
                displayController();
                break;
            default:
                break;
        }
    }

    /**
     * 显示图片
     */
    public void displayImage(String path) {
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imagePlayIv.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(this, "failed to get the image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 图片放大
     */
    private void enlarge() {
        if (scaleX < 2.0f && scaleY < 2.0f) {
            scaleX += 0.1f;
            scaleY += 0.1f;
            imagePlayIv.setScaleX(scaleX);
            imagePlayIv.setScaleY(scaleY);
        }
        else {
            scaleX = 2.0f;
            scaleY = 2.0f;
        }
    }

    /**
     * 图片缩小
     */
    private void lesson() {
        if (scaleX > 0.3f && scaleY > 0.3f) {
            scaleX -= 0.1f;
            scaleY -= 0.1f;
            imagePlayIv.setScaleX(scaleX);
            imagePlayIv.setScaleY(scaleY);
        }
        else {
            scaleX = 0.3f;
            scaleY = 0.3f;
        }
    }

    /**
     * 图片旋转
     */
    private void scale() {
        Bitmap bitmap = BitmapFactory.decodeFile(imageBean.getPath());
        Matrix matrix  = new Matrix();
        if (angel < 360) {
            angel += 90;
            matrix.setRotate(angel);
        }
        else {
            angel = 90;
            matrix.setRotate(angel);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imagePlayIv.setImageBitmap(bitmap);
    }

    /**
     * 上一张图片
     */
    private void previousImg() {
        mCurrentPosition--;
        if (mCurrentPosition >= 0) {
            imageBean = imageList.get(mCurrentPosition);
            displayImage(imageBean.getPath());
        }
        else {
            mCurrentPosition = 0;
            Toast.makeText(this, "没有上一张了！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下一张图片
     */
    private void nextImg() {
        mCurrentPosition++;
        if (mCurrentPosition < imageList.size()) {
            imageBean = imageList.get(mCurrentPosition);
            displayImage(imageBean.getPath());
        }
        else {
            mCurrentPosition = imageList.size() - 1;
            Toast.makeText(this, "已经是最后一张了！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示控制按钮
     */
    private void displayController() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_visibility);
        layout.setVisibility(View.VISIBLE);
        imagePlayIv.setFocusable(false);
        enlargeBtn.requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_visibility);
            layout.setVisibility(View.GONE);    //隐藏控制按钮
            imagePlayIv.setFocusable(true);
        }
        return super.dispatchKeyEvent(event);
    }

}
