package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
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
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class ListSongActivity extends AppCompatActivity {
    private RecyclerView rcvSongs;
    private SongAdapter songAdapter;
    ImageView imgList;
    Album album ;
    Artist artist;
    ArrayList<Song> listSong = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        imgList = findViewById(R.id.img_list);
        rcvSongs = findViewById(R.id.rcv_songs);
        songAdapter = new SongAdapter();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvSongs.setLayoutManager(linearLayoutManager);
        rcvSongs.setAdapter(songAdapter);
        dataIntent();
        getData();
        getHinhAnh();
        AppBarLayout appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float offsetPercentage = (float) Math.abs(verticalOffset) / (float) totalScrollRange;

                // Điều chỉnh tỷ lệ thu phóng của ImageView
                float scaleFactor = 1.0f - offsetPercentage;
                imgList.setScaleX(scaleFactor);
                imgList.setScaleY(scaleFactor);
            }
        });



//        anhxa();
    }

    private void getHinhAnh() {
        if(album != null){
            Glide.with(this).load(album.getAlbumURL())
                    .into(this.imgList);
        }
    }

    private void anhxa() {
        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        toolbar = findViewById(R.id.toolbarlist);
        floatingActionButton = findViewById(R.id.floatingactionbutton);

    }

    private void getData() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
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
        }


}