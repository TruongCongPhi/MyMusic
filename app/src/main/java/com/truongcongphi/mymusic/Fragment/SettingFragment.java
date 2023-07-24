package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truongcongphi.mymusic.Activity.SendEmailActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;


public class SettingFragment extends Fragment {
    TextView tvSendEmail;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        tvSendEmail = view.findViewById(R.id.tv_send_email);

        tvSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SendEmailActivity.class));
            }
        });
        return view;
    }


}