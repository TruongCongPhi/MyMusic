package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truongcongphi.mymusic.Adapter.ArtistAdapter;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.Class.PlayList;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment {
    private RecyclerView playlistRcv;
    private ArtistAdapter playlistAdapter;
    ArrayList<PlayList> playLists;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        playLists = new ArrayList<>();
        playlistRcv = view.findViewById(R.id.playlist_rcv);
        playlistAdapter = new ArtistAdapter();
        LinearLayoutManager artistLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        playlistRcv.setLayoutManager(artistLinearLayoutManager);
        playlistRcv.setAdapter(playlistAdapter);

        getPlaylistData();

        return view;
    }

    private void getPlaylistData() {

    }
}