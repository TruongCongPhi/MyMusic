package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.SongAdapter;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.Class.DaiyMix;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class ListSongActivity extends AppCompatActivity {
    private RecyclerView rcvSongs;
    private SongAdapter songAdapter;
    ImageView imgList, imgBack;
    Album album ;
    Artist artist;
    DaiyMix daiyMix;
    Top top;
    ArrayList<Song> listSong = new ArrayList<>();
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        addViews();
        dataIntent();
        getData();
        getHinhAnh();
        getTilte();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void addViews() {
        imgList = findViewById(R.id.img_list);
        imgBack = findViewById(R.id.btn_back);
        rcvSongs = findViewById(R.id.rcv_songs);
        songAdapter = new SongAdapter();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvSongs.setLayoutManager(linearLayoutManager);
        rcvSongs.setAdapter(songAdapter);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        floatingActionButton = findViewById(R.id.floatingactionbutton);
        toolbar = findViewById(R.id.toolbarlist);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }



    private void getTilte() {
        String title = "";

        if (album != null) {
            title = album.getAlbumName();
        } else if (artist != null) {
            title = artist.getName();
        }

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarTitleStyle);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarTitleStyle);
    }

    private void getHinhAnh() {
        if(album != null){
            Glide.with(this).load(album.getAlbumURL())
                    .into(this.imgList);
        }
    }



    private void getData() {
        if (album != null) {
            DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
            songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                        Song song = childSnapshot.getValue(Song.class);

                        if (song.getAlbumID().equals(album.getAlbumID())) {
                            listSong.add(song);
                            songAdapter.notifyDataSetChanged();
                        }
                    }
                    songAdapter.setData(listSong);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (daiyMix != null) {
            DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
            songsRef.orderByChild("mixId").equalTo(daiyMix.getMixId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song song = childSnapshot.getValue(Song.class);
                        Log.d("Song", "dailyMixId: " + song.getMixId());
                        Log.d("Daily", "dailyMixId: " + daiyMix.getMixId());
                        listSong.add(song);
                        songAdapter.notifyDataSetChanged();
                    }
                    songAdapter.setData(listSong);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra
                }
            });
        }

        if (artist != null) {
            DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference("songs");
            artistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song song = childSnapshot.getValue(Song.class);

                        if (artist.getListSongArtist().contains(song.getSongID())) {
                            listSong.add(song);
                            songAdapter.notifyDataSetChanged();
                        }

                    }
                    songAdapter.setData(listSong);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
        private void dataIntent() {
            Intent intent = getIntent();
            listSong.clear();
            if (intent.hasExtra("album")) {
                album = (Album) intent.getSerializableExtra("album");
                Toast.makeText(this,album.getAlbumID(),Toast.LENGTH_SHORT).show();
            }
            if (intent.hasExtra("artist")){
                artist = (Artist) intent.getSerializableExtra("artist");
                String listSongArtistString = TextUtils.join(", ", artist.getListSongArtist());
                Toast.makeText(this, listSongArtistString, Toast.LENGTH_LONG).show();            }
            if (intent.hasExtra("dailymix")) {
                daiyMix = (DaiyMix) intent.getSerializableExtra("dailymix");
                Toast.makeText(this,daiyMix.getMixId(),Toast.LENGTH_SHORT).show();
            }
            if (intent.hasExtra("top")) {
                top = (Top) intent.getSerializableExtra("top");
                Toast.makeText(this,top.getTopName(),Toast.LENGTH_SHORT).show();
            }
        }


}