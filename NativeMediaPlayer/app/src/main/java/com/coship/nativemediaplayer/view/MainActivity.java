package com.coship.nativemediaplayer.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.coship.nativemediaplayer.R;
import com.coship.nativemediaplayer.fragments.ImageHomeFragment;
import com.coship.nativemediaplayer.fragments.MusicHomeFragment;
import com.coship.nativemediaplayer.fragments.VideoHomeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn_image = (Button) findViewById(R.id.btn_image);
        final Button btn_music = (Button) findViewById(R.id.btn_music);
        final Button btn_video = (Button) findViewById(R.id.btn_video);

        btn_image.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        btn_music.requestFocus();
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        return true;
                    }
                }
                return false;
            }
        });

        btn_music.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        btn_image.requestFocus();
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        btn_video.requestFocus();
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return true;
                    }
                }
                return false;
            }
        });

        btn_video.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        btn_music.requestFocus();
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        return true;
                    }
                }
                return false;
            }
        });

        btn_image.setOnClickListener(this);
        btn_music.setOnClickListener(this);
        btn_video.setOnClickListener(this);

        replaceFragment(new ImageHomeFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image:
                replaceFragment(new ImageHomeFragment());
                break;
            case R.id.btn_music:
                replaceFragment(new MusicHomeFragment());
                break;
            case R.id.btn_video:
                replaceFragment(new VideoHomeFragment());
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.showhome_layout, fragment);
        transaction.commit();
    }
}