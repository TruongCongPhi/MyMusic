package com.truongcongphi.mymusic.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.truongcongphi.mymusic.ButtonAnimator.ButtonAnimator;
import com.truongcongphi.mymusic.R;


public class HomeLoginActivity extends AppCompatActivity {
    Button btnLoginFB, btnRegister, btnLoginGoogle;
    TextView btnLogin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);
        addViews();
        addEvents();
        addButtonAnimator();
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeLoginActivity.this, R.color.bg_color));
    }

    private void addViews() {
        btnLoginFB = (Button) findViewById(R.id.btn_loginFacebook);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnLoginGoogle = (Button) findViewById(R.id.btn_login_accountgoogle);
        btnLogin1 = findViewById(R.id.btn_login1);
    }

    //set các hiệu cho button
    private void addButtonAnimator() {
        ButtonAnimator buttonAnimator = new ButtonAnimator();
        btnRegister.setOnTouchListener(buttonAnimator);
        btnLoginGoogle.setOnTouchListener(buttonAnimator);
        btnLoginFB.setOnTouchListener(buttonAnimator);
        btnLogin1.setOnTouchListener(buttonAnimator);
    }

    public void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(HomeLoginActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(HomeLoginActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeLoginActivity.this);
        builder.setTitle("Bạn có chắc chắn muốn thoát");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}
