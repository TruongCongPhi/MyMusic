package com.truongcongphi.mymusic.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.truongcongphi.mymusic.Activity.MainActivity;
import com.truongcongphi.mymusic.R;

public class AccountFragment extends Fragment {
    ImageButton img_avt, ic_search, ic_add;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        img_avt = (ImageButton) view.findViewById(R.id.img_avt);
        ic_search = (ImageButton)  view.findViewById(R.id.ic_search);
        ic_add = (ImageButton) view.findViewById(R.id.ic_add);

        img_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountFragment2.class);
                startActivity(intent);
            }
        });

        return view;
    }
}