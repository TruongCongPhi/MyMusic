package com.truongcongphi.mymusic.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.truongcongphi.mymusic.R;

public class DieuKhoanActivity extends AppCompatActivity {
    ImageButton ic_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieu_khoan);
        ic_back = findViewById(R.id.ic_back);
        getWindow().setStatusBarColor(ContextCompat.getColor(DieuKhoanActivity.this, R.color.bg_color));
        addEvents();
    }
    private void addEvents() {
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
