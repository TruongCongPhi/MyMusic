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
import android.util.Log;
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
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.Class.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPasword;
    private Button btnLogin2;
    private ImageButton btnBack;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
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
                String email = edtEmail.getText().toString().trim();
                String password = edtPasword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                User user1 = new User();
                                user1.setEmail(email);
                                user1.setPassword(password);

                                // Kiểm tra xem tài khoản đã tồn tại trên cơ sở dữ liệu chưa
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            // Nếu tài khoản chưa tồn tại trên cơ sở dữ liệu thì lưu lên cơ sở dữ liệu
                                            mDatabase.child("users").child(uid).setValue(user1);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String image = dataSnapshot.child("imageUser").getValue(String.class);
                                            String name = dataSnapshot.child("name").getValue(String.class);

                                            // Lưu thông tin người dùng vào SessionManager
                                            sessionManager.saveUserCredentials(email, password, image, name);
                                            sessionManager.isLoggedIn();
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!" + name, Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                                    }
                                });

                                mDatabase.child("users")
                                        .child(uid)
                                        .child("playlists")
                                        .child("songliked")
                                        .child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                List<String> likedSongs = new ArrayList<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    String songId = dataSnapshot.getValue(String.class);
                                                    likedSongs.add(songId);
                                                }

                                                // Lưu danh sách các bài hát đã thích vào SessionManager
                                                sessionManager.saveLikedSongs(likedSongs);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                mDatabase.child("users")
                                        .child(uid)
                                        .child("playlists")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                List<String> playlists = new ArrayList<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    String playlistId = dataSnapshot.getKey();
                                                    playlists.add(playlistId);
                                                    Log.d("PlaylistFragment", "Playlist: " + playlistId);
                                                }
                                                // Lưu danh sách các album,artist... vào SessionManager
                                                sessionManager.savePlaylist(playlists);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại! Vui lòng kiểm tra email và mật khẩu.", Toast.LENGTH_SHORT).show();
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
        sessionManager = new SessionManager(this);
    }

}
