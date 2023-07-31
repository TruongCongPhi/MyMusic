package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.util.List;

public class CreatePlaylistActivity extends AppCompatActivity {
    private EditText edtPlaylistName;
    Button btnSave, btnExit;
    SessionManager sessionManager;
    FirebaseUser user;
    DatabaseReference databaseReference;
    Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        getWindow().setStatusBarColor(ContextCompat.getColor(CreatePlaylistActivity.this, R.color.mau_nen_play_nhac));

        edtPlaylistName = findViewById(R.id.edt_playlist_name);
        btnSave = findViewById(R.id.btn_save);
        btnExit = findViewById(R.id.btn_exit);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        sessionManager = new SessionManager(this);

        song = getIntent().getParcelableExtra("songplaylist");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePlaylist = String.valueOf(edtPlaylistName.getText());
                List<String> playlistId = sessionManager.getPlaylist();
                playlistId.add(namePlaylist);
                sessionManager.savePlaylist(playlistId);

                List<String> myPlaylistId = sessionManager.getmyPlaylist();

                if (!myPlaylistId.contains(namePlaylist)) {
                    myPlaylistId.add(namePlaylist);
                    sessionManager.saveMyPlaylist(myPlaylistId);

                    databaseReference.child("playlist_my").setValue(myPlaylistId);
                    databaseReference.child("playlists").child(namePlaylist).child("name").setValue(namePlaylist);

                    if (song != null) {
                        databaseReference.child("playlists").child(namePlaylist).child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> songIds = new ArrayList<>();

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String songId = snapshot.getValue(String.class);
                                        songIds.add(songId);
                                    }
                                }
                                songIds.add(song.getSongID());

                                // Lưu danh sách songIds đã cập nhật lên Firebase
                                databaseReference.child("playlists").child(namePlaylist).child("songs").setValue(songIds);

                                // Đóng BottomSheetDialog và chuyển tới MyPlaylistActivity
                                Intent intent = new Intent(v.getContext(), MyPlaylistActivity.class);
                                if (song != null) {
                                    intent.putExtra("myplaylist", song);
                                }
                                intent.putExtra("nameplaylist", namePlaylist);
                                v.getContext().startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý nếu có lỗi khi đọc dữ liệu từ Firebase
                                Toast.makeText(CreatePlaylistActivity.this, "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Intent intent = new Intent(v.getContext(), MyPlaylistActivity.class);
                        intent.putExtra("nameplaylist", namePlaylist);
                        v.getContext().startActivity(intent);
                        finish();
                    }
                } else
                    Toast.makeText(CreatePlaylistActivity.this, "Tên play list đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
