package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.truongcongphi.mymusic.Activity.SendEmailActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;


public class SettingFragment extends Fragment {

    ImageButton btn_back;
    LinearLayout ln_dangxuat;
    private SessionManager sessionManager;
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        ln_dangxuat = view.findViewById(R.id.ln_dangxuat);
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
        ln_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sessionManager.logoutUser();
                getActivity().finish();
            }
        });

    }


}