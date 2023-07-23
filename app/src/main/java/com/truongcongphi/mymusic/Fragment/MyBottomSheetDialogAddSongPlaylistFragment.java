package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBottomSheetDialogAddSongPlaylistFragment extends BottomSheetDialogFragment {

    private TextView tvExit;
    private Button btnAddToPlaylists,btnAddPlaylistNew;
    private ListView listView;
    private ArrayList<String> playlist;
    private ArrayAdapter<String> adapter;
    private String currentSongId;
    private HashMap<String, List<String>> playlistSongIdsMap = new HashMap<>();
    private SessionManager sessionManager;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_add_song_playlist, container, false);
        Bundle args = getArguments();
        Song song = args.getParcelable("song");
        currentSongId = song.getSongID();

        tvExit = view.findViewById(R.id.tv_exit);
        btnAddToPlaylists = view.findViewById(R.id.btn_add_playlist);
        btnAddPlaylistNew = view.findViewById(R.id.btn_add_playlist_new);
        listView = view.findViewById(R.id.listview_playlist);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        sessionManager = new SessionManager(getActivity());

        playlist = new ArrayList<>();
        for (String s : sessionManager.getmyPlaylist()) {
            playlist.add(s);
        }
        int songLikedIndex = playlist.indexOf("songliked");
        if (songLikedIndex != -1) {
            playlist.set(songLikedIndex, "Bài hát ưa thích");
        }

        adapter = new ArrayAdapter<>(getActivity(), R.layout.item_checkbox_playlist, R.id.itemName, playlist);
        listView.setAdapter(adapter);
        databaseReference.child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAllSongIdsInPlaylists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        addEvents();
        return view;
    }

    private void addEvents() {
        btnAddPlaylistNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getArguments();
                Song song = args.getParcelable("song");

                MyBottomSheetDialogAddPlaylistFragment bottomSheetDialog = MyBottomSheetDialogAddPlaylistFragment.newInstance(song);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), bottomSheetDialog.getTag());
                dismiss();

            }
        });

        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddToPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedItems = new ArrayList<>();
                int itemCount = listView.getCount();

                for (int i = 0; i < itemCount; i++) {
                    View itemView = listView.getChildAt(i);
                    if (itemView != null) {
                        CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                        TextView itemNameTextView = itemView.findViewById(R.id.itemName);

                        if (checkBox.isChecked()) {
                            selectedItems.add(itemNameTextView.getText().toString());
                        }
                    }
                }

                if (selectedItems.isEmpty()) {
                    dismiss();
                } else {
                    updatePlaylists(selectedItems);
                }
            }
        });
    }

    private void updatePlaylists(ArrayList<String> selectedItems) {
        for (String item : playlist) {
            boolean isPlaylistSelected = selectedItems.contains(item);

            List<String> songIdsInPlaylist = playlistSongIdsMap.get(item);
            boolean isSongInPlaylist = songIdsInPlaylist != null && songIdsInPlaylist.contains(currentSongId);

            if (isPlaylistSelected) {
                // Add the song to the playlist
                if (songIdsInPlaylist == null) {
                    songIdsInPlaylist = new ArrayList<>();
                }
                songIdsInPlaylist.add(currentSongId);
                databaseReference.child("playlists").child(item).child("songs").setValue(songIdsInPlaylist);
                Toast.makeText(getContext(), "Đã thêm vào danh sách phát " + item, Toast.LENGTH_SHORT).show();
            } else if (!isPlaylistSelected && isSongInPlaylist) {
                // Remove the song from the playlist
                if (songIdsInPlaylist != null) {
                    songIdsInPlaylist.remove(currentSongId);
                    databaseReference.child("playlists").child(item).child("songs").setValue(songIdsInPlaylist);
                    Toast.makeText(getContext(), "Đã xóa khỏi danh sách phát " + item, Toast.LENGTH_SHORT).show();
                }
            }
        }
        dismiss();
    }

    private void getAllSongIdsInPlaylists() {
        databaseReference.child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot playlistSnapshot : dataSnapshot.getChildren()) {
                    String playlistName = playlistSnapshot.getKey();
                    List<String> songIds = new ArrayList<>();

                    for (DataSnapshot songSnapshot : playlistSnapshot.child("songs").getChildren()) {
                        String songId = songSnapshot.getValue(String.class);
                        if (songId != null) {
                            songIds.add(songId);
                        }
                    }

                    playlistSongIdsMap.put(playlistName, songIds);
                    updateCheckBoxStateForPlaylist(playlistName, songIds.contains(currentSongId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCheckBoxStateForPlaylist(String playlistName, boolean isChecked) {
        int itemCount = listView.getCount();
        for (int i = 0; i < itemCount; i++) {
            View itemView = listView.getChildAt(i);
            if (itemView != null) {
                TextView itemNameTextView = itemView.findViewById(R.id.itemName);
                String currentPlaylistName = itemNameTextView.getText().toString();
                if (currentPlaylistName.equals(playlistName)) {
                    CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                    checkBox.setChecked(isChecked);
                    break;
                }
            }
        }
    }

    public static MyBottomSheetDialogAddSongPlaylistFragment newInstance(Song song) {
        MyBottomSheetDialogAddSongPlaylistFragment fragment = new MyBottomSheetDialogAddSongPlaylistFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        fragment.setArguments(args);
        return fragment;
    }
}
