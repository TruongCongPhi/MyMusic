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
import com.truongcongphi.mymusic.Activity.MainActivity;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Fragment.HomeFragment;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class shownhac extends AppCompatActivity {
    Button btnPushSong,btnUpdateSinger ,btnTop, btnDailyMix, btnUpdateUrlNameAlbum,btnCheck;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shownhac);
        btnPushSong = (Button) findViewById(R.id.btn_push_song);
        btnTop = findViewById(R.id.btn_top);
        btnDailyMix = (Button) findViewById(R.id.btn_dailymix);
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


        btnDailyMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDailyMixs();

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
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference albumsRef = database.getReference("tops");

                // Giả sử bạn đã có thông tin về URL và tên của các album
                Map<String, String> albumURLs = new HashMap<>();
                albumURLs.put("top001", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20%C4%91%E1%BB%81%20xu%E1%BA%A5t%20nh%E1%BA%A1c4%2Ftop1.jpg?alt=media&token=bee6e102-0cc8-472c-9177-1513b8a23f09");
                albumURLs.put("top002", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20%C4%91%E1%BB%81%20xu%E1%BA%A5t%20nh%E1%BA%A1c4%2Ftop2.jpg?alt=media&token=ca2c4de2-14f8-44b3-8ab7-9acb2370cacf");
                albumURLs.put("top003", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20%C4%91%E1%BB%81%20xu%E1%BA%A5t%20nh%E1%BA%A1c4%2Ftop3.jpg?alt=media&token=313bd4c7-a897-43d2-bd77-03ecb3f0ccb8");
                albumURLs.put("top004", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20%C4%91%E1%BB%81%20xu%E1%BA%A5t%20nh%E1%BA%A1c4%2Ftop4.jpg?alt=media&token=308bf193-d0ce-4cc8-84ed-66cb3bcf141f");
                // ...

                Map<String, String> albumNames = new HashMap<>();
                albumNames.put("top001", "đề xuất 1");
                albumNames.put("top002", "đề xuất 2");
                albumNames.put("top003", "đề xuất 3");
                albumNames.put("top004", "đề xuất 4");

                Map<String, String> id = new HashMap<>();
                id.put("top001", "top001");
                id.put("top002", "top002");
                id.put("top003", "top003");
                id.put("top004", "top004");


                // ...

                // Duyệt qua các entry của albumURLs và albumNames để cập nhật thông tin cho node albums
                for (Map.Entry<String, String> entry : albumURLs.entrySet()) {
                    String albumID = entry.getKey();
                    String albumURL = entry.getValue();
                    String albumName = albumNames.get(albumID);
                    String mixId = id.get(albumID);

                    DatabaseReference albumRef = albumsRef.child(albumID);
                    albumRef.child("topUrl").setValue(albumURL);
                    albumRef.child("topName").setValue(albumName);
                    albumRef.child("topId").setValue(mixId);

                }

            }
        });

    }

    private void updateDailyMixs() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference albumsRef = database.getReference("dailymixs");

        // Giả sử bạn đã có thông tin về URL và tên của các album
        Map<String, String> albumURLs = new HashMap<>();
        albumURLs.put("dailymix001", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20dailymix%2Fdailymix1.jpg?alt=media&token=0a049c14-b745-4134-8ecc-8481aefa2f19");
        albumURLs.put("dailymix002", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20dailymix%2Fdailymix2.jpg?alt=media&token=3f90b9a8-1bd9-4a6c-9c49-040c0001b50b");
        albumURLs.put("dailymix003", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20dailymix%2Fdailymix3.jpg?alt=media&token=e61c2cd1-7cd0-4fce-bb86-28cf61e3f0d9");
        albumURLs.put("dailymix004", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20dailymix%2Fdailymix4.jpg?alt=media&token=d8d3572c-aa9e-4acc-a93e-0b460746e499");
        // ...

        Map<String, String> albumNames = new HashMap<>();
        albumNames.put("dailymix001", "DailyMix 1");
        albumNames.put("dailymix002", "DailyMix 2");
        albumNames.put("dailymix003", "DailyMix 3");
        albumNames.put("dailymix004", "DailyMix 4");

        Map<String, String> id = new HashMap<>();
        id.put("dailymix001", "dailymix001");
        id.put("dailymix002", "dailymix002");
        id.put("dailymix003", "dailymix003");
        id.put("dailymix004", "dailymix004");


        // ...

        // Duyệt qua các entry của albumURLs và albumNames để cập nhật thông tin cho node albums
        for (Map.Entry<String, String> entry : albumURLs.entrySet()) {
            String albumID = entry.getKey();
            String albumURL = entry.getValue();
            String albumName = albumNames.get(albumID);
            String mixId = id.get(albumID);

            DatabaseReference albumRef = albumsRef.child(albumID);
            albumRef.child("url").setValue(albumURL);
            albumRef.child("mixName").setValue(albumName);
            albumRef.child("mixId").setValue(mixId);

        }


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
        startActivity(new Intent(this, MainActivity.class));
    }

    private void updateAlbumInfoInAlbums() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference albumsRef = database.getReference("albums");

        // Giả sử bạn đã có thông tin về URL và tên của các album
        Map<String, String> albumURLs = new HashMap<>();
        albumURLs.put("album001", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20album%2Fsontungmtp.jpg?alt=media&token=09935324-a622-46a0-84c3-2d193c21f071");
        albumURLs.put("album002", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20album%2F%E1%BA%A3nhMCK.jpg?alt=media&token=d02fd725-97de-4cca-bf56-744bb3aafe03");
        albumURLs.put("album003", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20album%2Flisa.jpg?alt=media&token=10d88c52-b490-47b8-b40f-1368bb5c2b9f");
        albumURLs.put("album004", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20album%2Fbts.jpg?alt=media&token=94141214-47dc-4ef0-a525-58803a4b9e60");
        albumURLs.put("album005", "https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/%E1%BA%A3nh%20album%2F22.jpg?alt=media&token=73900380-53b7-413b-b11c-e32ed255f900");
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

private void uploadSong() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songsRef1 = database.getReference("songs");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference songsRef = storageRef.child("songs");



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
                        newSong.put("mixId","");
                        newSong.put("topId","");

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