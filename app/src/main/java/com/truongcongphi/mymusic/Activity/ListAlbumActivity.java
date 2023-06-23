package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.SongAdapter;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class ListAlbumActivity extends AppCompatActivity {
    private RecyclerView rvSongs;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);

        // Khởi tạo RecyclerView và Adapter
        rvSongs = findViewById(R.id.rcv_songs);
        songAdapter = new SongAdapter();
        rvSongs.setAdapter(songAdapter);

        // Truy xuất cơ sở dữ liệu Firebase Realtime để lấy ra danh sách bài hát
        DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy ra danh sách bài hát được truyền qua từ Intent
                Intent intent = getIntent();
                List<Song> songs = (List<Song>) intent.getSerializableExtra("songs");

// Đưa dữ liệu vào Adapter
                songAdapter.setData(songs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}