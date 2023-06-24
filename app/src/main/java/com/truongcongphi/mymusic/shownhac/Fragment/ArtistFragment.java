package com.truongcongphi.mymusic.shownhac.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.ArtistAdapter;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {
    private RecyclerView artistRecyclerView;
    private ArtistAdapter artistAdapter;
    ArrayList<Artist> artists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        artists = new ArrayList<>();
        artistRecyclerView = view.findViewById(R.id.artist_rcv);
        artistAdapter = new ArtistAdapter();
        LinearLayoutManager artistLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        artistRecyclerView.setLayoutManager(artistLinearLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);

        getArtistData();

        return view;
    }

    private void getArtistData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Artist> artists = new ArrayList<>();
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    String artistImage = artistSnapshot.child("artistImage").getValue(String.class);
                    String artistName = artistSnapshot.child("artistName").getValue(String.class);

                    Artist artist = new Artist(artistImage, artistName);
                    List<String> listSongArtist = new ArrayList<>();
                    for (DataSnapshot songSnapshot : artistSnapshot.child("songs").getChildren()) {
                        String songId = songSnapshot.getValue(String.class);
                        listSongArtist.add(songId);
                    }
                    artist.setListSongArtist(listSongArtist); // Lưu danh sách các songID vào đối tượng Artist
                    artists.add(artist);
                }

                artistAdapter.setData(artists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }
        });
    }
}

