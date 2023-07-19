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
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class ListSongActivity extends AppCompatActivity {
    private RecyclerView rcvSongs;
    private SongAdapter songAdapter;
    ImageView imgList, imgBack,imgAddPlayList;
    Album album ;
    Artist artist;
    DaiyMix daiyMix;
    Top top;
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
        imgAddPlayList = findViewById(R.id.img_add_playlist);
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



    private void getTilte() {
        String title = "";

        if (album != null) {
            title = album.getAlbumName();
        }
        if (artist != null) {
            title = artist.getName();
        }
        if(daiyMix !=null){
            title = daiyMix.getMixName();
        }
        if (top!=null){
            title = top.getTopName();
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
        if(artist != null){
            Glide.with(this).load(artist.getImgURL())
                    .into(this.imgList);
        }
        if(daiyMix != null){
            Glide.with(this).load(daiyMix.getUrl())
                    .into(this.imgList);
        }
        if(top != null){
            Glide.with(this).load(top.getTopUrl())
                    .into(this.imgList);
        }

    }



    private void getData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (album != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> songPlaylist = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song song = childSnapshot.getValue(Song.class);
                        if (song.getAlbumID().equals(album.getAlbumID())) {
                            listSong.add(song);
                            songPlaylist.add(song.getSongID());
                            songAdapter.notifyDataSetChanged();



                        }
                    }
                    songAdapter.setData(listSong);
                    List<String> playlistId = sessionManager.getPlaylist();
                    String albumId = album.getAlbumID();
                    boolean isAlbum = playlistId.contains(albumId);
                    if(isAlbum){
                        imgAddPlayList.setImageResource(R.drawable.icon_add_task);
                    }
                    imgAddPlayList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            List<String> playlistId = sessionManager.getPlaylist();
                            String albumId = album.getAlbumID();
                            boolean isAlbum = playlistId.contains(albumId);

                            if(isAlbum){
                                playlistId.remove(albumId);
                                Toast.makeText(ListSongActivity.this,"Đã xóa khỏi danh sách phát",Toast.LENGTH_SHORT).show();
                                imgAddPlayList.setImageResource(R.drawable.icon_add);
                                databaseReference.child("users")
                                        .child(currentUser.getUid())
                                        .child("playlists")
                                        .child(album.getAlbumID())
                                        .removeValue();

                            }else {
                                playlistId.add(albumId);
                                imgAddPlayList.setImageResource(R.drawable.icon_add_task);
                                Toast.makeText(ListSongActivity.this, "Đã thêm vào danh sách phát",Toast.LENGTH_SHORT).show();
                                databaseReference.child("users")
                                        .child(currentUser.getUid())
                                        .child("playlists")
                                        .child(album.getAlbumID())
                                        .child("songs")
                                        .setValue(songPlaylist);
                                databaseReference.child("users")
                                        .child(currentUser.getUid())
                                        .child("playlists")
                                        .child(album.getAlbumID())
                                        .child("img").setValue(album.getAlbumURL());
                                databaseReference.child("users")
                                        .child(currentUser.getUid())
                                        .child("playlists")
                                        .child(album.getAlbumID())
                                        .child("name").setValue(album.getAlbumName());
                            }
                            sessionManager.savePlaylist(playlistId);






                        }
                    });
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
        if (top != null) {
            DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
            songsRef.orderByChild("top").equalTo(top.getTopId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song song = childSnapshot.getValue(Song.class);
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