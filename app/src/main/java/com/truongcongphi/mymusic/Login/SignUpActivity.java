package com.truongcongphi.mymusic.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPasword;
    private Button btnSignUp;
    ImageButton btnBack;
    private FirebaseAuth mAuth;
    boolean passwordCheck;
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
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(SignUpActivity.this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "tạo tài khoản thành công!",Toast.LENGTH_SHORT).show();
                            Intent intentHome = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intentHome);
                        }else {
                            Toast.makeText(getApplicationContext(), "tạo tài khoản không thành công!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,HomeLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });
        edtPasword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int right = 2;
                if(event.getAction()==MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtPasword.getRight() - edtPasword.getCompoundDrawables()[right].getBounds().width()) {
                        int selection = edtPasword.getSelectionEnd();
                        if (passwordCheck) {
                            edtPasword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_not_show_password, 0);
                            edtPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            passwordCheck = false;
                        } else {
                            edtPasword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0);
                            edtPasword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordCheck = true;
                        }
                        edtPasword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void addViews() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPasword = (EditText) findViewById(R.id.edt_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        btnBack = (ImageButton) findViewById(R.id.btn_back);
    }
}
