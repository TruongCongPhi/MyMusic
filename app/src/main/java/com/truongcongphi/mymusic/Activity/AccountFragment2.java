package com.truongcongphi.mymusic.Activity;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.User;
import com.truongcongphi.mymusic.R;

public class AccountFragment2 extends Fragment {
    ImageView imgUser;
    TextView tvName, tvGmail;
    ImageButton ic_back;
    private SessionManager sessionManager;
    User infor;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account2, container, false);
        imgUser = view.findViewById(R.id.img_avt);
        tvName = view.findViewById(R.id.txt_name);
        tvGmail = view.findViewById(R.id.txt_gmail);
        ic_back = view.findViewById(R.id.ic_back);

        sessionManager = new SessionManager(getActivity());
        infor = sessionManager.getLoggedInUser();

        addEvents();
        return view;
    }


    private void addEvents() {
        Glide.with(this).load(infor.getImageUser()).into(imgUser);
        tvName.setText(infor.getName());
        tvGmail.setText(infor.getEmail());
        imgUser.setOnClickListener(new View.OnClickListener() {
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

    }
}

