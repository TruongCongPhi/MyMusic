package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;


public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000; // Thời gian chờ đợi trước khi chuyển hướng (tính bằng mili giây)
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreenActivity.this, R.color.black));


        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    // Người dùng đã đăng nhập, chuyển hướng đến màn hình
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
                    Intent intent = new Intent(SplashScreenActivity.this, HomeLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
