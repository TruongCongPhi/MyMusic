package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.truongcongphi.mymusic.R;

public class DieuKhoanActivity extends AppCompatActivity {
    ImageButton ic_back;
    TextView txt_link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieu_khoan);
        ic_back = findViewById(R.id.ic_back);
        txt_link = (TextView) findViewById(R.id.txt_link);
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
        txt_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.spotify.com/vn-vi/legal/end-user-agreement/plain/"));
                startActivity(intent);
            }
        });
    }
}
