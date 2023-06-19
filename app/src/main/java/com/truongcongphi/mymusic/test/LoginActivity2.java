package com.truongcongphi.mymusic.test;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Activity.MainActivity;
import com.truongcongphi.mymusic.R;

public class LoginActivity2 extends AppCompatActivity {
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
                User user = new User(email, pass);
                user.setEmail(email);
                user.setPassword(pass);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intentHome = new Intent(LoginActivity2.this, MainActivity.class);
                            startActivity(intentHome);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");


                // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
                myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            // Người dùng chưa tồn tại trong cơ sở dữ liệu, thêm họ vào cơ sở dữ liệu
                            myRef.push().setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
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
        mAuth = FirebaseAuth.getInstance();
    }

}
