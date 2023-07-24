package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.truongcongphi.mymusic.R;

public class GetPasswordActivity extends AppCompatActivity {
    EditText edtEmail;
    Button btnGetPassword;
    ImageButton btnBack;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        edtEmail = findViewById(R.id.edt_email);
        btnGetPassword =findViewById(R.id.btn_get_pass);
        btnBack = findViewById(R.id.btn_back);
        mAuth= FirebaseAuth.getInstance();

        btnGetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                if (email.isEmpty()) {
                    edtEmail.setError("Vui lòng nhập địa chỉ email");
                    edtEmail.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GetPasswordActivity.this, "Vui lòng kiểm tra email để lấy lại mật khẩu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GetPasswordActivity.this, "Có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

    }
}