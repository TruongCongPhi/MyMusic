package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Adapter.SearchAdapter;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private ArrayList<Song> listSongView;
    private SearchView editTextSearch;

    private DatabaseReference databaseRef;
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        editTextSearch = view.findViewById(R.id.search_box);
        textView = view.findViewById(R.id.text_no_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listSongView = new ArrayList<>();
        adapter = new SearchAdapter(listSongView);
        recyclerView.setAdapter(adapter);

        getData();

        // Lắng nghe sự thay đổi trong EditText
        editTextSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText);
                return true;
            }
        });


        return view;
    }

    private void getData() {
        databaseRef = FirebaseDatabase.getInstance().getReference("songs");
        Query query = databaseRef;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song song = snapshot.getValue(Song.class);
                listSongView.add(song);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void filterSongs(String searchText) {
        ArrayList<Song> filteredList = new ArrayList<>();

        if (!TextUtils.isEmpty(searchText)) {
            String searchQuery = searchText.toLowerCase().trim();

            for (Song song : listSongView) {
                if (song.getSongName().toLowerCase().contains(searchQuery) || TextUtils.join(", ", song.getSingerName()).toLowerCase().contains(searchQuery)) {
                    filteredList.add(song);
                }
            }
        }

        adapter.setFilteredList(filteredList);
        if (filteredList.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
