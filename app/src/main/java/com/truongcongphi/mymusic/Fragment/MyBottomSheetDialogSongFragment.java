package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.List;

public class MyBottomSheetDialogSongFragment extends BottomSheetDialogFragment {
    FirebaseUser currentUser;
    TextView tvSongName,tvSingerName,tvSongLike, tvAddPlaylistSong;
    ImageView imgSong, iconLike;
    SessionManager sessionManager;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.layout_bottom_sheet_song, null);

        imgSong = contentView.findViewById(R.id.img_song);
        tvSongName = contentView.findViewById(R.id.tv_song_name);
        tvSingerName = contentView.findViewById(R.id.tv_singer_name);
        tvSongLike = contentView.findViewById(R.id.song_like);
        tvAddPlaylistSong =contentView.findViewById(R.id.add_playlist_song);
        iconLike = contentView.findViewById(R.id.icon_like);
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
                iconLike.setImageResource(R.drawable.icon_favorite_liked);
            }

            if (song != null) {
                // Hiển thị dữ liệu bài hát trong layout của BottomSheetDialog
                tvSongName.setText(song.getSongName());
                tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));
                Glide.with(this).load(song.getImageSong()).into(imgSong);
            }
            tvSongLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = currentUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(userId)
                            .child("playlists")
                            .child("songliked");

                    List<String> likedSongs = sessionManager.getLikedSongs();
                    String songId = song.getSongID();
                    boolean isLiked = likedSongs.contains(songId);

                    // Kiểm tra nếu bài hát đã có trong SessionManager
                    if (isLiked) {
                        // Xoá bài hát khỏi danh sách trong SessionManager
                        likedSongs.remove(song.getSongID());
                        tvSongLike.setText("Thích");
                        iconLike.setImageResource(R.drawable.icon_favorite);
                        Toast.makeText(getContext(),"Đã xóa khỏi trang Bài hát ưa thích",Toast.LENGTH_SHORT).show();

                    } else {
                        // Thêm bài hát vào danh sách trong SessionManager
                        likedSongs.add(song.getSongID());
                        tvSongLike.setText("Đã thích");
                        iconLike.setImageResource(R.drawable.icon_favorite_liked);
                        Toast.makeText(getContext(),"Đã thêm vào trang Bài hát ưa thích",Toast.LENGTH_SHORT).show();

                    }

                    // Lưu danh sách bài hát đã thay đổi vào SessionManager
                    sessionManager.saveLikedSongs(likedSongs);
                    // Lưu danh sách bài hát đã thay đổi lên Firebase


                    userRef.child("img").setValue("https://firebasestorage.googleapis.com/v0/b/music-2cd36.appspot.com/o/liked_songs.png?alt=media&token=6ac491d3-f73b-4be7-a664-af282e49c0a5");
                    userRef.child("name").setValue("Bài hát ưa thích");
                    userRef.child("songs").setValue(likedSongs);
                    dismiss();

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
        MyBottomSheetDialogAddSongPlaylistFragment dialog = new MyBottomSheetDialogAddSongPlaylistFragment();
        dialog.show(getActivity().getSupportFragmentManager(),dialog.getTag());
        dismiss();

    }


    public static MyBottomSheetDialogSongFragment newInstance(Song song) {
        MyBottomSheetDialogSongFragment fragment = new MyBottomSheetDialogSongFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        fragment.setArguments(args);
        return fragment;
    }

}
