package com.codingwithrufat.rxjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithrufat.rxjava.model.Posts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Posts> list = new ArrayList<>();

    public void setPosts(List<Posts> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void updatePost(Posts posts){
        list.set(list.indexOf(posts), posts);
        notifyItemChanged(list.indexOf(posts));
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_posts_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());

        if (list.get(position).getComments() == null){
            holder.progressBar.setVisibility(View.VISIBLE);
        }else{
            holder.progressBar.setVisibility(View.GONE);
            holder.numComments.setText(String.valueOf(list.get(position).getComments().size()));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, numComments;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            numComments = itemView.findViewById(R.id.num_comments);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }
    }
}
