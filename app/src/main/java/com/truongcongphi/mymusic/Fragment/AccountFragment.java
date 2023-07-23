package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import com.truongcongphi.mymusic.Activity.MyNotification;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

public class AccountFragment extends Fragment {
    ImageButton ic_search, imgAdd, btnTest;
    ImageView img_avt;
    Button btnSignOut;
    private SessionManager sessionManager;
    private FirebaseAuth mAuth;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        img_avt = view.findViewById(R.id.img_avt);
        ic_search = view.findViewById(R.id.ic_search);
        imgAdd = view.findViewById(R.id.img_add_playlist);
        btnSignOut = view.findViewById(R.id.btn_sign_out);
        btnTest = view.findViewById(R.id.ic_search);

        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(getActivity());
        addEvents();

        return view;
    }

    private void addEvents() {
        String imageUrl = sessionManager.getImage(); // Lấy địa chỉ ảnh từ SessionManager
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(img_avt); // Tải ảnh bằng Glide nếu địa chỉ ảnh khác null
        }
        img_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountFragment2 newFragment = new AccountFragment2();

                // Chuyển đổi sang fragment mới bằng FragmentTransaction
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment); // fragment_container là ID của container chứa fragment
                fragmentTransaction.addToBackStack(null); // (Tùy chọn) Đưa fragment hiện tại vào stack để quay lại khi nhấn nút Back
                fragmentTransaction.commit();

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất khỏi Firebase
                mAuth.signOut();
                // Xóa thông tin người dùng khỏi SessionManager
                sessionManager.logoutUser();
                getActivity().finish(); // Đóng Fragment hiện tại
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheetDialogAddPlaylistFragment myBottomSheetDialogPlaylistFragment = new MyBottomSheetDialogAddPlaylistFragment();
                myBottomSheetDialogPlaylistFragment.show(getActivity().getSupportFragmentManager(),myBottomSheetDialogPlaylistFragment.getTag());
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNotification myNotification = new MyNotification(getContext());
                myNotification.showNotification();
            }
        });

    }
}
