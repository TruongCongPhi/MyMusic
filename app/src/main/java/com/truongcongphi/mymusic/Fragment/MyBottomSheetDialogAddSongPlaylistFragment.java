package com.truongcongphi.mymusic.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.truongcongphi.mymusic.Activity.MyPlaylistActivity;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetDialogAddSongPlaylistFragment extends BottomSheetDialogFragment {


     TextView tvExit;
     Button btnAddPlaylist, btnAddPlaylistNew;
     ListView listView;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    SessionManager sessionManager;
    FirebaseUser user;
    DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_add_song_playlist, container, false);

        tvExit = view.findViewById(R.id.tv_exit);
        btnAddPlaylist = view.findViewById(R.id.btn_add_playlist);
        btnAddPlaylistNew = view.findViewById(R.id.btn_add_playlist_new);
        listView = view.findViewById(R.id.listview_playlist);



        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        sessionManager = new SessionManager(getActivity());
        items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        adapter = new ArrayAdapter<>(getActivity(), R.layout.item_checkbox_playlist, R.id.itemName, items);
        listView.setAdapter(adapter);
        addEvents();
        return view;
    }

    private void addEvents() {
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedItems = new ArrayList<>();

                // Lấy số lượng item trong ListView
                int itemCount = listView.getCount();

                // Duyệt qua tất cả các item trong ListView
                for (int i = 0; i < itemCount; i++) {
                    // Lấy view của từng item trong ListView
                    View itemView = listView.getChildAt(i);

                    // Kiểm tra xem item có tồn tại không
                    if (itemView != null) {
                        // Lấy reference đến CheckBox và TextView trong item
                        CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                        TextView itemNameTextView = itemView.findViewById(R.id.itemName);

                        // Kiểm tra xem CheckBox có được chọn hay không
                        if (checkBox.isChecked()) {
                            // Nếu được chọn, thêm tên item vào danh sách các item đã chọn
                            selectedItems.add(itemNameTextView.getText().toString());
                        }
                    }
                }

                if (selectedItems.isEmpty()) {
                    Toast.makeText(getContext(), "Chưa chọn danh sách phát nào", Toast.LENGTH_SHORT).show();
                } else {
                    String selectedItemsText = "Danh sách phát đã chọn: ";
                    for (String item : selectedItems) {
                        selectedItemsText += item + ", ";
                    }
                    selectedItemsText = selectedItemsText.substring(0, selectedItemsText.length() - 2); // Xóa dấu phẩy cuối cùng
                    Toast.makeText(getContext(), selectedItemsText, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
