package com.truongcongphi.mymusic.Login;

import android.content.Intent;
import android.os.Build;
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

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(LoginActivity.this,HomeLoginActivity.class);
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
    }

    private void addViews() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPasword = (EditText) findViewById(R.id.edt_password);
        btnLogin2 = (Button) findViewById(R.id.btn_login2);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        mAuth = FirebaseAuth.getInstance();
    }

}
