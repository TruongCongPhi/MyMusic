package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import java.util.List;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    FirebaseUser currentUser;
    TextView tvSongName,tvSingerName,tvSongLike, tvAddPlaylistSong;
    SessionManager sessionManager;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.layout_bottom_sheet, null);
        tvSongName = contentView.findViewById(R.id.tv_song_name);
        tvSingerName = contentView.findViewById(R.id.tv_singer_name);
        tvSongLike = contentView.findViewById(R.id.song_like);
        tvAddPlaylistSong =contentView.findViewById(R.id.add_playlist_song);
        sessionManager = new SessionManager(getActivity());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dialog.setContentView(contentView);


        // Truy cập dữ liệu bài hát từ getArguments()
        Bundle args = getArguments();
        if (args != null) {
            Song song = args.getParcelable("song");

            List<String> likedSongs = sessionManager.getLikedSongs();
            String songId = song.getSongID();
            boolean isLiked = likedSongs.contains(songId);

            // Đổi TextView thành "Đã thích" nếu bài hát đã thích
            if (isLiked) {
                tvSongLike.setText("Đã thích");
            }

            if (song != null) {
                // Hiển thị dữ liệu bài hát trong layout của BottomSheetDialog


                tvSongName.setText(song.getSongName());
                tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));
            }
            tvSongLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = currentUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(userId)
                            .child("playlists")
                            .child("songliked")
                            .child("songs");

                    // Kiểm tra nếu bài hát đã có trong SessionManager
                    if (isLiked) {
                        // Xoá bài hát khỏi danh sách trong SessionManager
                        likedSongs.remove(song.getSongID());
                        tvSongLike.setText("Thích");

                    } else {
                        // Thêm bài hát vào danh sách trong SessionManager
                        likedSongs.add(song.getSongID());
                        tvSongLike.setText("Đã thích");
                    }

                    // Lưu danh sách bài hát đã thay đổi vào SessionManager
                    sessionManager.saveLikedSongs(likedSongs);

                    // Lưu danh sách bài hát đã thay đổi lên Firebase
                    userRef.setValue(likedSongs);
                }
            });
        }

        tvAddPlaylistSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataPlaylistSong();
            }
        });
    }

    private void setDataPlaylistSong() {
    }


    public static MyBottomSheetDialogFragment newInstance(Song song) {
        MyBottomSheetDialogFragment fragment = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        fragment.setArguments(args);
        return fragment;
    }

}
