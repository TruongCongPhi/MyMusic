package com.truongcongphi.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPasword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addViews();
        addEvents();

    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = edtEmail.getText().toString();
                pass = edtPasword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(SignUpActivity.this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
                }
                mAuth.createUserWithEmailAndPassword(email,pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "tạo tài khoản thành công!",Toast.LENGTH_SHORT).show();
                            Intent intentHome = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intentHome);
                        }else {
                            Toast.makeText(getApplicationContext(), "tạo tài khoản không thành công!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void addViews() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPasword = (EditText) findViewById(R.id.edt_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
    }
}
