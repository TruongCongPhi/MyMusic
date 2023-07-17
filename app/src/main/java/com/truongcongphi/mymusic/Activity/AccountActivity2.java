package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.User;
import com.truongcongphi.mymusic.R;

public class AccountActivity2 extends AppCompatActivity {
    ImageView imgUser;
    TextView tvName, tvGmail;
    private SessionManager sessionManager;
    User infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account2);
        addViews();
        addEvents();
    }

    private void addEvents() {
        Glide.with(this).load(infor.getImageUser()).into(imgUser);
        tvName.setText(infor.getName());
        tvGmail.setText(infor.getEmail());
    }

    private void addViews() {
        imgUser = findViewById(R.id.img_avt);
        tvName = findViewById(R.id.txt_name);
        tvGmail = findViewById(R.id.txt_gmail);

        sessionManager = new SessionManager(this);
        infor = sessionManager.getLoggedInUser();
    }
}