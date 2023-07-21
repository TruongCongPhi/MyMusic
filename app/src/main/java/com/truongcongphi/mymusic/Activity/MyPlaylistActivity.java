package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.SongAdapter;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.Class.DaiyMix;
import com.truongcongphi.mymusic.Class.PlayList;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistActivity extends AppCompatActivity {
    private RecyclerView rcvSongs;
    private SongAdapter songAdapter;
    ImageView imgList, imgBack,imgAddPlayList;
    Album album ;
    Artist artist;
    DaiyMix daiyMix;
    Top top;
    PlayList playList;
    ArrayList<Song> listSong = new ArrayList<>();
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    FirebaseUser currentUser;
    SessionManager sessionManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_playlist);
        addViews();
        dataIntent();
        getData();
        getTilteAndImage();
        setupListeners();

    }
    private void setupListeners() {
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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        sessionManager = new SessionManager(this);

    }

    private void getTilteAndImage() {
        String title = "";
        if(playList != null){
            title = playList.getName();
            Glide.with(this).load(playList.getImg())
                    .error(R.drawable.music_note)
                    .into(this.imgList);

        }
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarTitleStyle);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarTitleStyle);
    }


    private void getData() {
        if(playList !=null){
            DatabaseReference artistRef = FirebaseDatabase.getInstance().getReference("songs");
            artistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> songPlaylist = new ArrayList<>();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song song = childSnapshot.getValue(Song.class);
                        if (playList.getListSongPlaylist().contains(song.getSongID())) {
                            listSong.add(song);
                            songPlaylist.add(song.getSongID());
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
        if (intent.hasExtra("playlist")) {
            playList = (PlayList) intent.getSerializableExtra("playlist");
            Toast.makeText(this,playList.getName(),Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("myplaylist")) {
            String namePlaylist = getIntent().getStringExtra("myplaylist");
            Toast.makeText(this,namePlaylist,Toast.LENGTH_SHORT).show();
        }
    }


}