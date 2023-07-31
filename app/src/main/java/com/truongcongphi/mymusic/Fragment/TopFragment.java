package com.truongcongphi.mymusic.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.TopAdapter;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {
    TextView tvTitleTop;
    View view;
    ArrayList<Top> tops;
    private RecyclerView topRecyclerview;
    private TopAdapter topAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, false);
        tvTitleTop = view.findViewById(R.id.tv_title_top);
        tops = new ArrayList<>();
        topRecyclerview = view.findViewById(R.id.top_rcv);
        topAdapter = new TopAdapter(getActivity(), tops);
        LinearLayoutManager albumLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        topRecyclerview.setLayoutManager(albumLinearLayoutManager);
        topRecyclerview.setAdapter(topAdapter);
        getTopData();
        return view;
    }

    private void getTopData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tops");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Top> top = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Top top1 = childSnapshot.getValue(Top.class);
                    String topId = childSnapshot.getKey();
                    top1.setTopId(topId);
                    top.add(top1);
                }
                topAdapter.setData((ArrayList<Top>) top);
                tvTitleTop.setText("Bảng xếp hạng Music");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }
        });
    }
}