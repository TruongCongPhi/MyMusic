package com.truongcongphi.mymusic.Fragment;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.truongcongphi.mymusic.Activity.BenThuBaActivity;
import com.truongcongphi.mymusic.Activity.ChinhSachActivity;
import com.truongcongphi.mymusic.Activity.DieuKhoanActivity;
import com.truongcongphi.mymusic.Activity.EditProfileActivity;
import com.truongcongphi.mymusic.Activity.SendEmailActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

public class AccountFragment2 extends Fragment {
    ImageView imgUser;
    TextView tvName, tvGmail;
    ImageView ic_back;
    Button btn_edit, btnSignOut;

    LinearLayout ln_phanmem, ln_dieukien, ln_chinhsach, ln_hotro;

    private SessionManager sessionManager;

    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account2, container, false);
        imgUser = view.findViewById(R.id.img_avt);
        tvName = view.findViewById(R.id.txt_name);
        tvGmail = view.findViewById(R.id.txt_gmail);
        ic_back = view.findViewById(R.id.ic_back);
        btn_edit = view.findViewById(R.id.btn_edit);
        ln_phanmem = view.findViewById(R.id.ln_phanmembent3);
        ln_dieukien = view.findViewById(R.id.ln_dieukien);
        ln_chinhsach = view.findViewById(R.id.ln_chinhsach);
        ln_hotro = view.findViewById(R.id.ln_hotro);
        btnSignOut = view.findViewById(R.id.btn_dangxuat);
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(getActivity());


        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.mau_nen_play_nhac));



        addEvents();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String imageUrl = sessionManager.getImage(); // Lấy địa chỉ ảnh từ SessionManager
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imgUser);
        }else imgUser.setImageResource(R.drawable.ic_user);

        tvName.setText(sessionManager.getName());
        tvGmail.setText(sessionManager.getEmail());
    }


    private void addEvents() {

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sessionManager.logoutUser();
                getActivity().finish();
            }
        });

        ln_phanmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), BenThuBaActivity.class));
            }
        });
        ln_dieukien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), DieuKhoanActivity.class));
            }
        });
        ln_chinhsach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), ChinhSachActivity.class));
            }
        });
        ln_hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(requireContext(), SendEmailActivity.class));
            }
        });



    }


}

