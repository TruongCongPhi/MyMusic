package com.truongcongphi.mymusic.shownhac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class shownhac extends AppCompatActivity {
    Button btnPushSong , btnAlbum;
    TextView tvAlbum;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownhac);
        btnPushSong = (Button) findViewById(R.id.btn_push_song);
        btnAlbum = (Button) findViewById(R.id.btn_album);



        btnPushSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("songs");
                Query query = ref.orderByChild("albumID").equalTo("2");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String songName = snapshot.child("songName").getValue(String.class);
                            Toast.makeText(shownhac.this, songName, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Error", "onCancelled", databaseError.toException());
                    }
                });
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
}