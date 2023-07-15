package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.layout_bottom_sheet, null);
        dialog.setContentView(contentView);

        // Truy cập dữ liệu bài hát từ getArguments()
        Bundle args = getArguments();
        if (args != null) {
            Song song = args.getParcelable("song");
            if (song != null) {
                // Hiển thị dữ liệu bài hát trong layout của BottomSheetDialog
                TextView tvSongName = contentView.findViewById(R.id.tv_song_name);
                TextView tvSingerName = contentView.findViewById(R.id.tv_singer_name);

                tvSongName.setText(song.getSongName());
                tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));
            }
        }
    }

    public static MyBottomSheetDialogFragment newInstance(Song song) {
        MyBottomSheetDialogFragment fragment = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        fragment.setArguments(args);
        return fragment;
    }
}
