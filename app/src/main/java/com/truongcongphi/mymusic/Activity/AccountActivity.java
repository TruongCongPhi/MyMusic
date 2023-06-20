package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.test.User;

import java.io.File;

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