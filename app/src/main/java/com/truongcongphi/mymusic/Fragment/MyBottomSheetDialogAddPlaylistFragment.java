package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Activity.MyPlaylistActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetDialogAddPlaylistFragment extends BottomSheetDialogFragment {


    private EditText edtPlaylistName;
    SessionManager sessionManager;
    FirebaseUser user;
    DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_playlist, container, false);

        edtPlaylistName = view.findViewById(R.id.edt_playlist_name);
        Button btnSave = view.findViewById(R.id.btnSave);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        sessionManager = new SessionManager(getActivity());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePlaylist = String.valueOf(edtPlaylistName.getText());
                List<String> playlistId = sessionManager.getPlaylist();
                playlistId.add(namePlaylist);

                List<String> myPlaylistId = sessionManager.getmyPlaylist();

                if(!myPlaylistId.contains(namePlaylist)){
                    myPlaylistId.add(namePlaylist);
                    sessionManager.saveMyPlaylist(myPlaylistId);

                    databaseReference.child("playlist_my").setValue(myPlaylistId);
                    databaseReference.child("playlists").child(namePlaylist).child("name").setValue(namePlaylist);

                    Bundle args = getArguments();

                    Song song = args.getParcelable("song");
                    databaseReference.child("playlists").child(namePlaylist).child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> songIds = new ArrayList<>();

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String songId = snapshot.getValue(String.class);
                                    songIds.add(songId);
                                }
                            }

                            // Thêm songId mới vào danh sách songIds
                            songIds.add(song.getSongID());

                            // Lưu danh sách songIds đã cập nhật lên Firebase
                            databaseReference.child(namePlaylist).child("songs").setValue(songIds);

                            // Tiếp tục xử lý dữ liệu của bạn nếu cần
                            // ...

                            // Đóng BottomSheetDialog và chuyển tới MyPlaylistActivity
                            Intent intent = new Intent(v.getContext(), MyPlaylistActivity.class);
                            intent.putExtra("myplaylist", song);
                            intent.putExtra("nameplaylist", namePlaylist);
                            v.getContext().startActivity(intent);

                            dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý nếu có lỗi khi đọc dữ liệu từ Firebase
                            Toast.makeText(getContext(), "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else Toast.makeText(getContext(),"Tên play list đã tồn tại",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public static MyBottomSheetDialogAddPlaylistFragment newInstance(Song song) {
        MyBottomSheetDialogAddPlaylistFragment fragment = new MyBottomSheetDialogAddPlaylistFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        fragment.setArguments(args);
        return fragment;
    }


}
