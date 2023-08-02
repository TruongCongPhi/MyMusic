package com.truongcongphi.mymusic.Fragment;
import static androidx.core.app.ActivityCompat.finishAffinity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.truongcongphi.mymusic.Activity.MainActivity;
import com.truongcongphi.mymusic.R;

public class HomeFragment extends Fragment {

    ImageButton img_setting;



    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        img_setting = view.findViewById(R.id.img_setting);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.bg_color));


        addEvents();
        return view;


    }

    private void addEvents() {
        img_setting.setOnClickListener(new View.OnClickListener() {
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
    }

    public boolean onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn có chắc chắn muốn thoát");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý khi người dùng chọn "Yes" để thoát
                finishAffinity(getActivity());
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();

        return true;
    }
}