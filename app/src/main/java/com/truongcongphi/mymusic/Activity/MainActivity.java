package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.demoalbum.Album;
import com.truongcongphi.mymusic.demoalbum.AlbumAdapter;
import com.truongcongphi.mymusic.demoalbum.Artist;
import com.truongcongphi.mymusic.demoalbum.ArtistAdapter;
import com.truongcongphi.mymusic.demoalbum.Category;
import com.truongcongphi.mymusic.demoalbum.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private RecyclerView albumRecyclerView;
    private AlbumAdapter albumAdapter;

    private RecyclerView artistRecyclerView;
    private ArtistAdapter artistAdapter;
    private TextView tvShow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Chuyển đổi childSnapshot thành một đối tượng Artist
                    Artist artist = childSnapshot.getValue(Artist.class);
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