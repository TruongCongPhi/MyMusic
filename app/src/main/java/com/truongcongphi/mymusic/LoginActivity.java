package com.truongcongphi.mymusic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.truongcongphi.mymusic.ButtonAnimator.ButtonAnimator;


public class LoginActivity extends AppCompatActivity {
    Button btnLoginFB, btnLoginPNumber , btnRegister, btnLoginGoogle, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();
        addButtonAnimator();


    }



    private void addViews() {
        btnLoginFB = (Button) findViewById(R.id.btn_loginFacebook);
        btnLoginPNumber = (Button) findViewById(R.id.btn_login_phonenumber);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnLoginGoogle = (Button) findViewById(R.id.btn_login_accountgoogle);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    //set các hiệu cho button
    public void addButtonAnimator(){
        btnRegister.setOnTouchListener(new ButtonAnimator(btnRegister));
        btnLoginPNumber.setOnTouchListener(new ButtonAnimator(btnLoginPNumber));
        btnLoginGoogle.setOnTouchListener(new ButtonAnimator(btnLoginGoogle));
        btnLoginFB.setOnTouchListener(new ButtonAnimator(btnLoginFB));
        btnLogin.setOnTouchListener(new ButtonAnimator(btnLogin));
    }
// chuyển trang đăng kí
    public void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });



    }
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
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
