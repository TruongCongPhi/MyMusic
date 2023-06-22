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
import com.truongcongphi.mymusic.Activity.MainActivity;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.test.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class shownhac extends AppCompatActivity {
    Button btnPushSong,btnUpdateSinger , btnAlbum, btnUpdateUrlNameAlbum,btnCheck;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownhac);
        btnPushSong = (Button) findViewById(R.id.btn_push_song);
        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnUpdateSinger = (Button) findViewById(R.id.btn_update_singer);
        btnUpdateUrlNameAlbum = (Button) findViewById(R.id.btn_update_url_name_album);
        btnCheck= (Button) findViewById(R.id.btn_check);



        btnPushSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });
        btnUpdateSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSinger();

            }
        });


        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSingerNameInAlbums();

            }
        });
        btnUpdateUrlNameAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlbumInfoInAlbums();

            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

    }

    private void updateSinger() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songsRef = database.getReference("songs");
        DatabaseReference artistsRef = database.getReference("artists");

        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> addedSingers = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String songId = snapshot.getKey();
                    List<String> singerNames = (List<String>) snapshot.child("singerName").getValue();

                    for (String singerName : singerNames) {
                        String[] singers = singerName.split(", ");

                        for (String singer : singers) {
                            if (!addedSingers.contains(singer)) {
                                Map<String, Object> newArtist = new HashMap<>();
                                newArtist.put("artistName", singer);
                                newArtist.put("artistImageUrl", "your_artist_image_url_here");
                                newArtist.put("songIds", Arrays.asList(songId));

                                artistsRef.child(singer).setValue(newArtist);

                                addedSingers.add(singer);
                            } else {
                                artistsRef.child(singer).child("songIds").get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        List<String> songIds = (List<String>) task.getResult().getValue();
                                        songIds.add(songId);

                                        artistsRef.child(singer).child("songIds").setValue(songIds);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Lấy dữ liệu từ node songs thất bại", databaseError.toException());
            }
        });
    }

    private void Check() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void updateAlbumInfoInAlbums() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference albumsRef = database.getReference("albums");

        // Giả sử bạn đã có thông tin về URL và tên của các album
        Map<String, String> albumURLs = new HashMap<>();
        albumURLs.put("album001", "URL của album 1");
        albumURLs.put("album002", "https://firebasestorage.googleapis.com/v0/b/loginmusic-1f591.appspot.com/o/%E1%BA%A3nh%20album%2Fmck.jpg?alt=media&token=8325d713-1920-46a8-b281-a84909e2c16c");
        albumURLs.put("album003", "https://firebasestorage.googleapis.com/v0/b/loginmusic-1f591.appspot.com/o/%E1%BA%A3nh%20album%2Flisa.jpg?alt=media&token=ab4f8242-8b08-498b-91b3-b2b743233f39");
        albumURLs.put("album004", "URL của album 2");
        albumURLs.put("album005", "https://firebasestorage.googleapis.com/v0/b/loginmusic-1f591.appspot.com/o/%E1%BA%A3nh%20album%2F22.jpg?alt=media&token=544787ea-65d8-47b7-9bea-bef4b1d16a72");
        // ...

        Map<String, String> albumNames = new HashMap<>();
        albumNames.put("album001", "MTP");
        albumNames.put("album002", "99%");
        albumNames.put("album003", "LALISA");
        albumNames.put("album004", "Take two");
        albumNames.put("album005", "22");


        // ...

        // Duyệt qua các entry của albumURLs và albumNames để cập nhật thông tin cho node albums
        for (Map.Entry<String, String> entry : albumURLs.entrySet()) {
            String albumID = entry.getKey();
            String albumURL = entry.getValue();
            String albumName = albumNames.get(albumID);

            DatabaseReference albumRef = albumsRef.child(albumID);
            albumRef.child("albumURL").setValue(albumURL);
            albumRef.child("albumName").setValue(albumName);
        }
    }
private void updateSingerNameInAlbums() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songsRef = database.getReference("songs");

    songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Duyệt qua các phần tử của dataSnapshot
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                // Chuyển đổi childSnapshot thành một đối tượng Song
                Song song = childSnapshot.getValue(Song.class);
                // Lấy ra thông tin về albumID và tên ca sĩ
                String albumID = song.getAlbumID();
                String singerName = song.getSingerName();

                // Cập nhật tên ca sĩ trong node albums
                DatabaseReference albumRef = database.getReference("albums").child(albumID);
                albumRef.child("singerName").setValue(singerName);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Xử lý lỗi khi truy xuất dữ liệu
        }
    });
}
//private void uploadSong() {
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    StorageReference songsRef = storageRef.child("songs");
//
//    songsRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//        @Override
//        public void onSuccess(ListResult listResult) {
//            final int[] countIDCount = {1};
//
//            for (StorageReference item : listResult.getItems()) {
//                String[] parts = item.getName().split(" - ");
//                String songName = parts[0];
//                String singerName = parts[1];
//                String duration = parts[2];
//                String album = parts[3].replace(".mp3", "");
//                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String audioUrl = uri.toString();
//
//                        // Lưu trữ thông tin về bài hát trong cơ sở dữ liệu
//                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference songsRef = database.getReference("songs");
//
//                        Map<String, Object> newSong = new HashMap<>();
//                        newSong.put("songName", songName);
//                        newSong.put("singerName", singerName);
//                        newSong.put("albumID", album);
//                        newSong.put("duration", duration);
//                        newSong.put("url", audioUrl);
//
//                        // Sử dụng định dạng mới cho key
//                        String key = String.format(Locale.US, "song%03d", countIDCount[0]);
//                        songsRef.child(key).setValue(newSong);
//
//                        countIDCount[0]++;
//                    }
//                });
//            }
//        }
//    });
//}
private void uploadSong() {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference songsRef = storageRef.child("songs");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songsRef1 = database.getReference("songs");

    songsRef1.removeValue(new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if (databaseError != null) {
                Log.e("Firebase", "Xóa node songs thất bại", databaseError.toException());
            } else {
                Log.i("Firebase", "Node songs đã được xóa");
            }
        }
    });


    songsRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
        @Override
        public void onSuccess(ListResult listResult) {
            final int[] countIDCount = {1};

            for (StorageReference item : listResult.getItems()) {
                String[] parts = item.getName().split(" - ");
                String songName = parts[0];
                String[] singers = parts[1].split(", ");
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
                        newSong.put("singerName", Arrays.asList(singers));
                        newSong.put("albumID", album);
                        newSong.put("duration", duration);
                        newSong.put("url", audioUrl);

                        // Sử dụng định dạng mới cho key
                        String key = String.format(Locale.US, "song%03d", countIDCount[0]);
                        songsRef.child(key).setValue(newSong);

                        countIDCount[0]++;
                    }
                });
            }
        }
    });
}
}