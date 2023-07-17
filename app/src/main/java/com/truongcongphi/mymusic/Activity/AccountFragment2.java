package com.truongcongphi.mymusic.Activity;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.User;
import com.truongcongphi.mymusic.R;

public class AccountFragment2 extends Fragment {
    ImageView imgUser;
    TextView tvName, tvGmail;
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

        sessionManager = new SessionManager(getActivity());
        infor = sessionManager.getLoggedInUser();

        addEvents();
        return view;
    }


    private void addEvents() {
        Glide.with(this).load(infor.getImageUser()).into(imgUser);
        tvName.setText(infor.getName());
        tvGmail.setText(infor.getEmail());
    }
}

