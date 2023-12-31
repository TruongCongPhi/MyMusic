package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.truongcongphi.mymusic.Activity.SendEmailActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingFragment extends Fragment {

    ImageButton btn_back;
    LinearLayout  ln_phanmem, ln_dieukien, ln_chinhsach, ln_hotro;
    Button btnSignOut;

    CircleImageView img_avt;
    TextView txt_name;

    private SessionManager sessionManager;
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ben_thu_ba, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        img_avt = view.findViewById(R.id.img_avt);
        txt_name = view.findViewById(R.id.txt_name);
        ln_phanmem = view.findViewById(R.id.ln_phanmembent3);
        ln_dieukien = view.findViewById(R.id.ln_dieukien);
        ln_chinhsach = view.findViewById(R.id.ln_chinhsach);
        ln_hotro = view.findViewById(R.id.ln_hotro);
        btnSignOut = view.findViewById(R.id.btn_dangxuat);
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(getActivity());
        addEvents();
        return view;
    }

    private void addEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ln_phanmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ln_dieukien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ln_chinhsach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ln_hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(requireContext(), SendEmailActivity.class));
            }
        });


    }
    public void onResume() {
        super.onResume();
        String imageUrl = sessionManager.getImage(); // Lấy địa chỉ ảnh từ SessionManager
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(img_avt);
        }else img_avt.setImageResource(R.drawable.ic_user);

        txt_name.setText(sessionManager.getName());

    }



}