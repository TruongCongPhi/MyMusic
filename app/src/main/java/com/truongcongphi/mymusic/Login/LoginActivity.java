package com.truongcongphi.mymusic.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.truongcongphi.mymusic.MainActivity;
import com.truongcongphi.mymusic.R;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPasword;
    private Button btnLogin2;
    private ImageButton btnBack;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();

    }

    private void addEvents() {
        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = edtEmail.getText().toString();
                pass = edtPasword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(email,pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Đăng nhập khoản thành công!",Toast.LENGTH_SHORT).show();
                            Intent intentHome = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intentHome);
                        }else {
                            Toast.makeText(getApplicationContext(), "Đăng nhập không thành công!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addViews() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPasword = (EditText) findViewById(R.id.edt_password);
        btnLogin2 = (Button) findViewById(R.id.btn_login2);
        btnBack = (ImageButton) findViewById(R.id.image_btn_exit);
        mAuth = FirebaseAuth.getInstance();
    }

}
