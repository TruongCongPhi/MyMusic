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
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Adapter.AlbumAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {
    View view;
    ArrayList<Album> albums;
    private RecyclerView albumRecyclerView;
    private AlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);

        albums = new ArrayList<>();
        albumRecyclerView = view.findViewById(R.id.album_rcv);
        albumAdapter = new AlbumAdapter(getActivity(),albums);
        LinearLayoutManager albumLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        albumRecyclerView.setLayoutManager(albumLinearLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);

        getAlbumData();

        return view;
    }

    private void getAlbumData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("albums");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Album> albums = new ArrayList<>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Album album = childSnapshot.getValue(Album.class);
                    String albumID = childSnapshot.getKey();
                    album.setAlbumID(albumID);
                    albums.add(album);
                }

                albumAdapter.setData(albums);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }
        });
    }
}
