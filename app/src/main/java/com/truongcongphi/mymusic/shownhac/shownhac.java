package com.truongcongphi.mymusic.shownhac;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.truongcongphi.mymusic.Fragment.HomeFragment;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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
                updateArtists();

            }
        });


        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateSingerNameInAlbums();

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

//    Nếu bạn muốn key của các bài hát trong songs phải là số thứ tự tăng dần, bạn có thể sử dụng một biến đếm để lưu trữ số thứ tự hiện tại và tăng giá trị của nó mỗi khi bạn thêm một bài hát mới vào songs. Ví dụ:

    private void updateArtists() {
        DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
        DatabaseReference artistsRef = FirebaseDatabase.getInstance().getReference("artists");

        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, List<String>> artistSongsMap = new HashMap<>();

                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String songId = songSnapshot.getKey();
                    List<String> singerNames = songSnapshot.child("singerName").getValue(new GenericTypeIndicator<List<String>>() {});

                    for (String singerName : singerNames) {
                        String[] individualSingers = singerName.split(",");

                        for (String individualSinger : individualSingers) {
                            String trimmedSingerName = individualSinger.trim();
                            if (!artistSongsMap.containsKey(trimmedSingerName)) {
                                artistSongsMap.put(trimmedSingerName, new ArrayList<>());
                            }
                            artistSongsMap.get(trimmedSingerName).add(songId);
                        }
                    }
                }

                for (Map.Entry<String, List<String>> entry : artistSongsMap.entrySet()) {
                    String artistName = entry.getKey();
                    List<String> songs = entry.getValue();

                    // Tạo nút con cho từng ca sĩ
                    DatabaseReference artistRef = artistsRef.child(artistName);

                    // Lưu thông tin về ca sĩ
                    artistRef.child("artistName").setValue(artistName);
                    artistRef.child("artistImage").setValue("url_to_artist_image"); // Thay thế bằng URL ảnh ca sĩ thực tế

                    // Thêm danh sách songID vào mảng songs của ca sĩ
                    artistRef.child("songs").setValue(songs);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


    private void Check() {
        startActivity(new Intent(this, HomeFragment.class));
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
//private void updateSingerNameInAlbums() {
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference songsRef = database.getReference("songs");
//
//    songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            // Duyệt qua các phần tử của dataSnapshot
//            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                // Chuyển đổi childSnapshot thành một đối tượng Song
//                Song song = childSnapshot.getValue(Song.class);
//                // Lấy ra thông tin về albumID và tên ca sĩ
//                String albumID = song.getAlbumID();
//                String singerName = song.getSingerName();
//
//                // Cập nhật tên ca sĩ trong node albums
//                DatabaseReference albumRef = database.getReference("albums").child(albumID);
//                albumRef.child("singerName").setValue(singerName);
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            // Xử lý lỗi khi truy xuất dữ liệu
//        }
//    });
//}
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

                        String key = String.format(Locale.US, "song%03d", countIDCount[0]);
                        Map<String, Object> newSong = new HashMap<>();
                        newSong.put(("songID"),key);
                        newSong.put("songName", songName);
                        newSong.put("singerName", Arrays.asList(singers));
                        newSong.put("albumID", album);
                        newSong.put("duration", duration);
                        newSong.put("url", audioUrl);
                        newSong.put("imageSong","url hình bài hát");
                        newSong.put("like","lượt thích");

                        // Sử dụng định dạng mới cho key
                        songsRef.child(key).setValue(newSong);

                        countIDCount[0]++;
                    }
                });
            }
        }
    });
}
}