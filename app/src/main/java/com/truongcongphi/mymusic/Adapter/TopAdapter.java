package com.truongcongphi.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.ListSongActivity;
import com.truongcongphi.mymusic.ButtonAnimator.ButtonAnimator;
import com.truongcongphi.mymusic.Class.DaiyMix;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {
    Context context;
    ArrayList<Top> list;

    public TopAdapter(Context context, ArrayList<Top> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home,parent,false);

        return new TopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopViewHolder holder, int position) {
        Top top = list.get(position);
        if(top == null){
            return;
        }


        Glide.with(holder.itemView.getContext())
                .load(top.getTopUrl())
                .error(R.drawable.music_note)
                .into(holder.imgTop);

        holder.tvTop.setText(top.getTopName());

    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public void setData(ArrayList<Top> tops) {
        this.list = tops;
        notifyDataSetChanged();
    }

    public class TopViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgTop;
        private TextView tvTop;
        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTop = itemView.findViewById(R.id.img_item);
            tvTop = itemView.findViewById(R.id.tv_tilte1);
            ButtonAnimator buttonAnimator = new ButtonAnimator();
            itemView.setOnTouchListener(buttonAnimator);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ListSongActivity.class);
                    // Gắn dữ liệu album vào Intent
                    intent.putExtra("top", list.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
