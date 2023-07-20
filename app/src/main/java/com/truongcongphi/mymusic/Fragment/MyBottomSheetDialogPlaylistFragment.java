package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.zzx;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.truongcongphi.mymusic.Activity.ListSongActivity;
import com.truongcongphi.mymusic.Activity.MyPlaylistActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyBottomSheetDialogPlaylistFragment extends BottomSheetDialogFragment {


    private EditText edtPlaylistName;
    SessionManager sessionManager;
    FirebaseUser user;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_playlist, container, false);

        edtPlaylistName = view.findViewById(R.id.edt_playlist_name);
        Button btnSave = view.findViewById(R.id.btnSave);
        sessionManager = new SessionManager(getActivity());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePlaylist = String.valueOf(edtPlaylistName.getText());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                List<String> playlistId = sessionManager.getPlaylist();
                playlistId.add(namePlaylist);

                List<String> myPlaylistId = sessionManager.getmyPlaylist();
                myPlaylistId.add(namePlaylist);
                sessionManager.saveMyPlaylist(myPlaylistId);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                databaseReference.child("myplaylist").setValue(myPlaylistId);
                databaseReference.child("playlists").child(namePlaylist).child("name").setValue(namePlaylist);

                Intent intent = new Intent(v.getContext(), MyPlaylistActivity.class);
                // Gắn dữ liệu album vào Intent
                intent.putExtra("myplaylist",namePlaylist);
                v.getContext().startActivity(intent);
                dismiss();
            }
        });

        return view;
    }


}
