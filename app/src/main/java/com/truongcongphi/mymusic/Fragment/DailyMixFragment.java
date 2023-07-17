package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Adapter.AlbumAdapter;

import com.truongcongphi.mymusic.Adapter.DailyMixAdapter;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Class.DaiyMix;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class DailyMixFragment extends Fragment {
    ArrayList<DaiyMix> dailyMixes ;
    RecyclerView dailyMixRecyclerView;
    DailyMixAdapter dailyMixAdapter;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_mix, container, false);

        dailyMixes = new ArrayList<>();
        dailyMixRecyclerView = view.findViewById(R.id.daily_mix_rcv);
        dailyMixAdapter = new DailyMixAdapter(getActivity(),dailyMixes);
        LinearLayoutManager albumLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        dailyMixRecyclerView.setLayoutManager(albumLinearLayoutManager);
        dailyMixRecyclerView.setAdapter(dailyMixAdapter);

        getDailyMixData();

        return view;
    }

    private void getDailyMixData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("dailymix");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DaiyMix> daiyMixList = new ArrayList<>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    DaiyMix daiyMix = childSnapshot.getValue(DaiyMix.class);
                    String dailyMixID = childSnapshot.getKey();
                    daiyMix.setMixId(dailyMixID);
                    daiyMixList.add(daiyMix);

                }
                dailyMixAdapter.setData((ArrayList<DaiyMix>) daiyMixList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi tại đây
            }
        });
    }
}
