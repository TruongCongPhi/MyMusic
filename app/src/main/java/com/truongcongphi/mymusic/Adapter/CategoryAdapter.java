package com.truongcongphi.mymusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truongcongphi.mymusic.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategogyViewHolder> {
    private Context mcontext;
    private List<Category> mListCategory;

    public CategoryAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }
    public void setData(List<Category> list){
        this.mListCategory=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategogyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategogyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategogyViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if(category == null){
            return;
        }
        holder.tvNameCategory.setText(category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL,false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);



        AlbumAdapter albumAdapter = new AlbumAdapter();
        albumAdapter.setData(category.getAlbums());
        holder.recyclerView.setAdapter(albumAdapter);


    }

    @Override
    public int getItemCount() {
        if (mListCategory !=null){
            return mListCategory.size();
        }
        return 0;
    }

    public class CategogyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameCategory;
        private RecyclerView recyclerView;

        public CategogyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCategory = itemView.findViewById(R.id.tv_categogy);
            recyclerView = itemView.findViewById(R.id.rcv_category);
        }
    }
}
