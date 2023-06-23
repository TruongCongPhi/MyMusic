package com.truongcongphi.mymusic.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Adapter.AlbumAdapter;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.Adapter.ArtistAdapter;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends AppCompatActivity {
    private RecyclerView albumRecyclerView;
    private AlbumAdapter albumAdapter;

    private RecyclerView artistRecyclerView;
    private ArtistAdapter artistAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Khởi tạo RecyclerView và Adapter cho danh sách album
        albumRecyclerView = findViewById(R.id.album_rcv);
        albumAdapter = new AlbumAdapter();
        LinearLayoutManager albumLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        albumRecyclerView.setLayoutManager(albumLinearLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);

        // Khởi tạo RecyclerView và Adapter cho danh sách nghệ sĩ
        artistRecyclerView = findViewById(R.id.artist_rcv);
        artistAdapter = new ArtistAdapter();
        LinearLayoutManager artistLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        artistRecyclerView.setLayoutManager(artistLinearLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);

        getAlbumData();
        getArtistData();

    }

    private void getAlbumData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("albums");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Tạo một danh sách để chứa các đối tượng Album
                List<Album> albums = new ArrayList<>();

                // Duyệt qua các phần tử của dataSnapshot
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Chuyển đổi childSnapshot thành một đối tượng Album
                    Album album = childSnapshot.getValue(Album.class);
                    // Lấy ra albumID từ childSnapshot
                    String albumID = childSnapshot.getKey();
                    // Đưa albumID vào đối tượng Album
                    album.setAlbumID(albumID);
                    // Thêm đối tượng Album vào danh sách
                    // Thêm đối tượng Album vào danh sách
                    albums.add(album);
                }

                // Cập nhật dữ liệu cho AlbumAdapter
                albumAdapter.setData(albums);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }

        });
    }

    private void getArtistData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Tạo một danh sách để chứa các đối tượng Artist
                List<Artist> artists = new ArrayList<>();

                // Duyệt qua các phần tử của dataSnapshot
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin của ca sĩ từ dataSnapshot
                    String artistImage = artistSnapshot.child("artistImage").getValue(String.class);
                    String artistName = artistSnapshot.child("artistName").getValue(String.class);


                    // Tạo một đối tượng Artist với thông tin lấy được
                    Artist artist = new Artist(artistImage,artistName);

                    // Thêm đối tượng Artist vào danh sách
                    artists.add(artist);
                }

                // Cập nhật dữ liệu cho ArtistAdapter
                artistAdapter.setData(artists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }
        });
    }
}