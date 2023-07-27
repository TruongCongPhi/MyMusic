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
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class DailyMixAdapter extends RecyclerView.Adapter<DailyMixAdapter.DailyMixViewHolder> {
    Context context;
    ArrayList<DaiyMix> list;

    public DailyMixAdapter(Context context, ArrayList<DaiyMix> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DailyMixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home,parent,false);

        return new DailyMixViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyMixViewHolder holder, int position) {
        DaiyMix daiyMix = list.get(position);
        if(daiyMix == null){
            return;
        }


            Glide.with(holder.itemView.getContext())
                    .load(daiyMix.getUrl())
                    .into(holder.imgDailyMix);

        holder.tvDailyMix.setText(daiyMix.getMixName());
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }
    public void setData(ArrayList<DaiyMix> dailyMixs) {

        this.list = dailyMixs;
        notifyDataSetChanged();
    }

    public class DailyMixViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgDailyMix;
        private TextView tvDailyMix;
        public DailyMixViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDailyMix = itemView.findViewById(R.id.img_item);
            tvDailyMix = itemView.findViewById(R.id.tv_tilte1);
            ButtonAnimator buttonAnimator = new ButtonAnimator();
            itemView.setOnTouchListener(buttonAnimator);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ListSongActivity.class);
                    // Gắn dữ liệu album vào Intent
                    intent.putExtra("dailymix", list.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
