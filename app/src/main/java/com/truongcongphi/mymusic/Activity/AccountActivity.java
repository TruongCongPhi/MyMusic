package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.truongcongphi.mymusic.R;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {
    TextView tvUserName, tvEmail;
    EditText edtUserName,edtGetURL;
    Button btnUpdateName, btnSignOut, btnGetURL, btnPushSong;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnUpdateName = (Button) findViewById(R.id.btn_user_name);
        edtUserName = (EditText) findViewById(R.id.edt_user_name);
        btnGetURL = (Button) findViewById(R.id.btn_get_url);
        edtGetURL = (EditText) findViewById(R.id.edt_get_url);
        btnPushSong = (Button) findViewById(R.id.btn_push_song);
        mAuth = FirebaseAuth.getInstance();


        showInfor();

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        btnGetURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getURL();
            }
        });
        btnPushSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });

    }

    private void uploadSong() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference songsRef = storageRef.child("songs");

        songsRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final int[] countIDCOunt = {1};

                for (StorageReference item : listResult.getItems()) {

                    String[] parts = item.getName().split(" - ");
                    String songName = parts[0];
                    String singerName = parts[1];
                    String duration = parts[2];
                    String album = parts[3].replace(".mp3", "");
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String audioUrl = uri.toString();

                            // Lưu trữ thông tin về bài hát trong cơ sở dữ liệu
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference songsRef = database.getReference("songs");

                            Map<String, Object> newSong = new HashMap<>();
                            newSong.put("songName", songName);
                            newSong.put("singerName", singerName);
                            newSong.put("albumID", album);
                            newSong.put("duration", duration);
                            newSong.put("url", audioUrl);



                            songsRef.child(String.valueOf(countIDCOunt[0])).setValue(newSong);

                            countIDCOunt[0]++;
                        }
                    });

                }
            }
        });
    }


    private void getURL() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("songs");
        String name = String.valueOf(storageRef.child("Mình Dành Cho Nhau Nỗi Buồn.mp3").getRoot());
        edtGetURL.setText(name);
    }

    private void showInfor() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user !=null){
             String userEmail = user.getEmail();
             tvEmail.setText(userEmail);
        }
    }

    private void updateName() {
        FirebaseUser user =mAuth.getCurrentUser();
        String userID=user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String name = edtUserName.getText().toString();
                tvUserName.setText(name);
                myRef.child(userID).child("name").setValue(name);



    }

    private void signOut() {
        finish();
        startActivity(new Intent(AccountActivity.this, HomeLoginActivity.class));
    }

}