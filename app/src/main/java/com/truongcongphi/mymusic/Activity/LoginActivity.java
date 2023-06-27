package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.Class.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPasword;
    private Button btnLogin2;
    private ImageButton btnBack;
    private FirebaseAuth mAuth;
    boolean passwordCheck;
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
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                            Intent intentHome = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intentHome);
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            User user1 = new User();
                            user1.setEmail(email);
                            user1.setPassword(pass);

                            // Kiểm tra xem tài khoản đã tồn tại trên cơ sở dữ liệu chưa
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        // Nếu tài khoản chưa tồn tại trên cơ sở dữ liệu thì lưu email lên cơ sở dữ liệu
                                        mDatabase.child("users").child(uid).setValue(user1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });

            }

        });


        // button quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,HomeLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // ẩn-hiện password
        edtPasword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int right = 2;
                if(event.getAction()==MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtPasword.getRight() - edtPasword.getCompoundDrawables()[right].getBounds().width()) {
                        int selection = edtPasword.getSelectionEnd();
                        if (passwordCheck) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                edtPasword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_not_show_password, 0);
                            }
                            edtPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            passwordCheck = false;
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                edtPasword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0);
                            }
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

        // bắt buộc nhap đủ email-pass mới hiển thị button
        edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEnabled = !TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(edtPasword.getText().toString());
                btnLogin2.setEnabled(isEnabled);
                if (isEnabled) {
                    btnLogin2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                } else {
                    btnLogin2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#423F3E")));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPasword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEnabled =!TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(edtEmail.getText().toString());
                btnLogin2.setEnabled(isEnabled);
                if (isEnabled) {
                    btnLogin2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                } else {
                    btnLogin2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#423F3E")));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void addViews() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPasword = (EditText) findViewById(R.id.edt_password);
        btnLogin2 = (Button) findViewById(R.id.btn_login2);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        mAuth = FirebaseAuth.getInstance();
    }

}
