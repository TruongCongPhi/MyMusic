package com.truongcongphi.mymusic.Activity;

import android.content.Intent;


import android.os.Bundle;
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
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtPassword2, edtName;
    private Button btnSignUp;
    ImageButton btnBack;
    private FirebaseAuth mAuth;
    boolean passwordCheck;
    private SessionManager sessionManager;
    private boolean passwordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addViews();
        addEvents();
        getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.bg_color));

    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, name, pass2;
                email = edtEmail.getText().toString();
                pass = edtPassword.getText().toString();
                name = edtName.getText().toString();
                pass2 =edtPassword2.getText().toString();

                if (email.isEmpty()){
                    edtEmail.setError("Vui lòng nhập địa chỉ email");
                    edtEmail.requestFocus();
                    return;
                }
                if (name.isEmpty()){
                    edtName.setError("Vui lòng nhập tên người dùng");
                    edtName.requestFocus();
                    return;
                }
                if (pass.isEmpty()){
                    edtPassword.setError("Vui lòng nhập mật khẩu");
                    edtPassword.requestFocus();
                    return;
                }
                if (pass2.isEmpty()){
                    edtPassword2.setError("Vui lòng nhập lại mật khẩu");
                    edtPassword2.requestFocus();
                    return;
                }

                if (!pass2.contains(pass)){
                    edtPassword2.setError("Không trùng với mật khẩu trên");
                    edtPassword2.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            Toast.makeText(getApplicationContext(), "tạo tài khoản thành công!",Toast.LENGTH_SHORT).show();
                            sessionManager.saveUserCredentials(email, pass,null,name);
                            mDatabase.child("users").child(uid).child("email").setValue(email);
                            mDatabase.child("users").child(uid).child("password").setValue(pass);
                            mDatabase.child("users").child(uid).child("name").setValue(name);

                            List<String> playlists = new ArrayList<>();
                            String songliked = "Bài hát ưa thích";
                            playlists.add(songliked);
                            sessionManager.saveMyPlaylist(playlists);

                            Intent intentHome = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intentHome);
                            finish();
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

        //Show/Hide Password
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int right = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[right].getBounds().width()) {
                        passwordVisible = !passwordVisible;

                        // Đổi hình ảnh của Drawable phía cuối
                        if (passwordVisible) {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0);
                        } else {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_not_show_password, 0);
                        }

                        edtPassword.setTransformationMethod(passwordVisible ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                        edtPassword.setSelection(edtPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
        edtPassword2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int right = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtPassword2.getRight() - edtPassword2.getCompoundDrawables()[right].getBounds().width()) {
                        passwordVisible = !passwordVisible;

                        // Đổi hình ảnh của Drawable phía cuối
                        if (passwordVisible) {
                            edtPassword2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0);
                        } else {
                            edtPassword2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_not_show_password, 0);
                        }

                        edtPassword2.setTransformationMethod(passwordVisible ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                        edtPassword2.setSelection(edtPassword2.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });



    }
    private void addViews() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignUp = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        btnBack = findViewById(R.id.btn_back);
        edtPassword2 = findViewById(R.id.edt_password2);
        edtName = findViewById(R.id.edt_name);

        sessionManager = new SessionManager(this);
    }
}
