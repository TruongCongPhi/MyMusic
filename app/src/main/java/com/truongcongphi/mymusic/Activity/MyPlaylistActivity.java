package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.truongcongphi.mymusic.Class.PlayList;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistActivity extends AppCompatActivity {
    private RecyclerView rcvSongs;
    private SongAdapter songAdapter;
    ImageView imgList, imgBack, imgMovePlayList;

    PlayList playList;
    ArrayList<Song> listSong = new ArrayList<>();
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    SessionManager sessionManager;
    Song song;
    String namePlaylist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_playlist);
        getWindow().setStatusBarColor(ContextCompat.getColor(MyPlaylistActivity.this, R.color.mau_nen_play_nhac));

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

        imgMovePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPlaylistActivity.this);
                builder.setTitle("Xoá danh sách phát này");
                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (namePlaylist != null) {
                            DatabaseReference playlistRef = databaseReference.child("playlists").child(namePlaylist);
                            playlistRef.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Failed to delete the playlist
                                            Toast.makeText(MyPlaylistActivity.this, "Failed to delete playlist!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            sessionManager.removePlaylist(namePlaylist);
                            databaseReference.child("playlist_my").setValue(sessionManager.getmyPlaylist());
                        } else if (playList != null) {
                            DatabaseReference playlistRef = databaseReference.child("playlists").child(playList.getId());
                            playlistRef.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyPlaylistActivity.this, "Failed to delete playlist!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            sessionManager.removePlaylist(playList.getName());
                            databaseReference.child("playlist_my").setValue(sessionManager.getmyPlaylist());
                        }


                    }
                });
                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });
    }

    private void addViews() {
        imgList = findViewById(R.id.img_list);
        imgBack = findViewById(R.id.btn_back);
        rcvSongs = findViewById(R.id.rcv_songs);
        imgMovePlayList = findViewById(R.id.img_move_playlist);
        songAdapter = new SongAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvSongs.setLayoutManager(linearLayoutManager);
        rcvSongs.setAdapter(songAdapter);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        toolbar = findViewById(R.id.toolbarlist);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        sessionManager = new SessionManager(this);
    }

    private void getTilteAndImage() {
        if (playList != null) {
            collapsingToolbarLayout.setTitle(playList.getId());
            Glide.with(this)
                    .load(playList.getImg())
                    .error(R.drawable.music_note)
                    .into(imgList);
        } else if (song != null) {
            collapsingToolbarLayout.setTitle(namePlaylist);
        } else collapsingToolbarLayout.setTitle(namePlaylist);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarTitleStyle);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarTitleStyle);
    }

    private void getData() {
        if (song != null) {
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("songs");
            playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> songPlaylist = new ArrayList<>();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Song songg = childSnapshot.getValue(Song.class);
                        if (song.getSongID().contains(songg.getSongID())) {
                            listSong.add(song);
                            songPlaylist.add(song.getSongID());
                            songAdapter.notifyDataSetChanged();
                        }
                    }
                    songAdapter.setData(listSong, namePlaylist);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if (playList != null) {
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("songs");
            playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    songAdapter.setData(listSong, playList.getName());
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
        }
        if (intent.hasExtra("nameplaylist")) {
            namePlaylist = intent.getStringExtra("nameplaylist");
            Toast.makeText(this, namePlaylist, Toast.LENGTH_SHORT).show();

        }
        if (intent.hasExtra("myplaylist")) {
            song = getIntent().getParcelableExtra("myplaylist");
        }
    }
}