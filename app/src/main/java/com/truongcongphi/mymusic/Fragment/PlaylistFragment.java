package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Activity.EditProfileActivity;
import com.truongcongphi.mymusic.Adapter.ArtistAdapter;
import com.truongcongphi.mymusic.Adapter.PlaylistAdapter;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.Class.PlayList;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;


public class PlaylistFragment extends Fragment {
    private DatabaseReference userRef;

    private RecyclerView playlistRcv;
    private PlaylistAdapter playlistAdapter;
    ArrayList<PlayList> playLists;
    FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        playLists = new ArrayList<>();
        playlistRcv = view.findViewById(R.id.playlist_rcv);
        playlistAdapter = new PlaylistAdapter(getActivity(),playLists);
        LinearLayoutManager artistLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        playlistRcv.setLayoutManager(artistLinearLayoutManager);
        playlistRcv.setAdapter(playlistAdapter);

        getPlaylistData();

        return view;
    }

    private void getPlaylistData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId)
                    .child("playlists");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot playlistSnapshot : dataSnapshot.getChildren()) {
                            String id = playlistSnapshot.getKey();
                            String img = playlistSnapshot.child("img").getValue(String.class);
                            String name = playlistSnapshot.child("name").getValue(String.class);

                            List<String> listSongPlaylist = new ArrayList<>();
                            for (DataSnapshot songSnapshot : playlistSnapshot.child("songs").getChildren()) {
                                String songId = songSnapshot.getValue(String.class);
                                listSongPlaylist.add(songId);
                            }

                            PlayList playList = new PlayList(id, img, name);
                            playList.setListSongPlaylist(listSongPlaylist);
                            Log.d("PlaylistFragment", "Playlist ID: " + id);
                            Log.d("PlaylistFragment", "Playlist Name: " + name);
                            Log.d("PlaylistFragment", "Playlist Image: " + img);
                            Log.d("PlaylistFragment", "Playlist Songs: " + listSongPlaylist.toString());
                            playLists.add(playList);
                        }
                    }
                    playlistAdapter.setData(playLists);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                }
            });
        }
    }
}