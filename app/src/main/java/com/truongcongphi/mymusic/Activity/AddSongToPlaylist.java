package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSongToPlaylist extends AppCompatActivity {
    private ImageView imgExit;
    private Button btnAddToPlaylists,btnAddPlaylistNew;
    private ListView listView;
    private ArrayList<String> playlist;
    private ArrayAdapter<String> adapter;
    private String currentSongId;
    private HashMap<String, List<String>> playlistSongIdsMap = new HashMap<>();
    private SessionManager sessionManager;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    Song song;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);
        getSong();
        addViews();
    }

    private void getSong() {
        song = getIntent().getParcelableExtra("songplaylist");
        if (song != null) {
            currentSongId = song.getSongID();
        }
    }

    private void addViews() {

        imgExit =findViewById(R.id.img_exit);
        btnAddToPlaylists = findViewById(R.id.btn_add_playlist);
        btnAddPlaylistNew = findViewById(R.id.btn_add_playlist_new);
        listView = findViewById(R.id.listview_playlist);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        sessionManager = new SessionManager(this);

        playlist = new ArrayList<>();
        for (String s : sessionManager.getmyPlaylist()) {
            playlist.add(s);
        }

        adapter = new ArrayAdapter<>(this, R.layout.item_checkbox_playlist, R.id.itemName, playlist);
        listView.setAdapter(adapter);
        databaseReference.child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAllSongIdsInPlaylists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSongToPlaylist.this, "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        addEvents();
    }

    private void addEvents() {
        btnAddPlaylistNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreatePlaylistActivity.class);
                // Gắn dữ liệu album vào Intent
                intent.putExtra("songplaylist", song);
                v.getContext().startActivity(intent);
                finish();

            }
        });

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddToPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedItems = new ArrayList<>();
                int itemCount = listView.getCount();

                for (int i = 0; i < itemCount; i++) {
                    View itemView = listView.getChildAt(i);
                    if (itemView != null) {
                        CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                        TextView itemNameTextView = itemView.findViewById(R.id.itemName);

                        if (checkBox.isChecked()) {
                            selectedItems.add(itemNameTextView.getText().toString());
                        }
                    }
                }
                updatePlaylists(selectedItems);
            }
        });
    }

    private void updatePlaylists(ArrayList<String> selectedItems) {
        for (String item : playlist) {
            boolean isPlaylistSelected = selectedItems.contains(item);

            List<String> songIdsInPlaylist = playlistSongIdsMap.get(item);

            if (isPlaylistSelected) {
                // Add the song to the playlist
                if (songIdsInPlaylist == null) {
                    songIdsInPlaylist = new ArrayList<>();
                }
                if (!songIdsInPlaylist.contains(currentSongId)) {
                    songIdsInPlaylist.add(currentSongId);
                    databaseReference.child("playlists").child(item).child("songs").setValue(songIdsInPlaylist);
                    Toast.makeText(this, "Đã thêm vào danh sách phát " + item, Toast.LENGTH_SHORT).show();
                }
            } else {
                // Remove the song from the playlist
                if (songIdsInPlaylist != null && songIdsInPlaylist.contains(currentSongId)) {
                    songIdsInPlaylist.remove(currentSongId);
                    databaseReference.child("playlists").child(item).child("songs").setValue(songIdsInPlaylist);
                    Toast.makeText(this, "Đã xóa khỏi danh sách phát " + item, Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
    }
    private void getAllSongIdsInPlaylists() {
        databaseReference.child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot playlistSnapshot : dataSnapshot.getChildren()) {
                    String playlistName = playlistSnapshot.getKey();
                    List<String> songIds = new ArrayList<>();

                    for (DataSnapshot songSnapshot : playlistSnapshot.child("songs").getChildren()) {
                        String songId = songSnapshot.getValue(String.class);
                        if (songId != null) {
                            songIds.add(songId);
                        }
                    }

                    playlistSongIdsMap.put(playlistName, songIds);
                    updateCheckBoxStateForPlaylist(playlistName, songIds.contains(currentSongId));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSongToPlaylist.this, "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateCheckBoxStateForPlaylist(String playlistName, boolean isChecked) {
        int itemCount = listView.getCount();
        for (int i = 0; i < itemCount; i++) {
            View itemView = listView.getChildAt(i);
            if (itemView != null) {
                TextView itemNameTextView = itemView.findViewById(R.id.itemName);
                String currentPlaylistName = itemNameTextView.getText().toString();
                if (currentPlaylistName.equals(playlistName)) {
                    CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                    checkBox.setChecked(isChecked);
                    break;
                }
            }
        }
    }
}
