package com.neo.kit.main;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.neo.kit.R;


/**
 * @author neo.duan
 * @date 2019/08/07
 * @desc splash
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            MainActivity.start(SplashActivity.this);
            finish();
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }
}
